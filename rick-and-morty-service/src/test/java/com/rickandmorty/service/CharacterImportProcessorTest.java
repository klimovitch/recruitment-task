package com.rickandmorty.service;

import com.rickandmorty.dto.CharacterApiDto;
import com.rickandmorty.entity.CharacterEntity;
import com.rickandmorty.mapper.CharacterMapper;
import com.rickandmorty.repository.CharacterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacterImportProcessorTest {

    @Mock
    private CharacterRepository repository;

    @Mock
    private CharacterMapper mapper;

    @InjectMocks
    private CharacterImportProcessor processor;

    @Test
    void process_should_skip_when_dto_is_null() {
        processor.process(null);

        verifyNoInteractions(repository, mapper);
    }

    @Test
    void process_should_create_new_entity_when_not_exists() {
        CharacterApiDto dto = new CharacterApiDto();
        dto.setId(100L);

        var mapped = new CharacterEntity();

        when(repository.findByExternalId(100L)).thenReturn(Optional.empty());
        when(mapper.updateFromApi(null, dto)).thenReturn(mapped);
        when(repository.save(mapped)).thenReturn(mapped);

        processor.process(dto);

        verify(repository).findByExternalId(100L);
        verify(mapper).updateFromApi(null, dto);
        verify(repository).save(mapped);
    }

    @Test
    void process_should_update_existing_entity() {
        var dto = new CharacterApiDto();
        dto.setId(200L);

        var existing = new CharacterEntity();
        existing.setId(11L);

        var updated = new CharacterEntity();
        updated.setId(11L);

        when(repository.findByExternalId(200L)).thenReturn(Optional.of(existing));
        when(mapper.updateFromApi(existing, dto)).thenReturn(updated);
        when(repository.save(updated)).thenReturn(updated);

        processor.process(dto);

        verify(repository).findByExternalId(200L);
        verify(mapper).updateFromApi(existing, dto);
        verify(repository).save(updated);
    }

    @Test
    void process_should_propagate_exception_from_mapper() {
        var dto = new CharacterApiDto();
        dto.setId(300L);

        var existing = new CharacterEntity();

        when(repository.findByExternalId(300L)).thenReturn(Optional.of(existing));

        doThrow(new RuntimeException("mapping failed")).when(mapper).updateFromApi(existing, dto);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> processor.process(dto)
        );

        assertEquals("mapping failed", ex.getMessage());
    }

    @Test
    void process_should_retry_on_duplicate_key_and_succeed() {
        var dto = new CharacterApiDto();
        dto.setId(400L);

        var newEntity = new CharacterEntity();
        var existingEntity = new CharacterEntity();
        existingEntity.setId(42L);

        when(repository.findByExternalId(400L))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(existingEntity));

        when(mapper.updateFromApi(null, dto)).thenReturn(newEntity);
        when(mapper.updateFromApi(existingEntity, dto)).thenReturn(existingEntity);

        when(repository.save(newEntity))
                .thenThrow(new DataIntegrityViolationException("Duplicate key"));
        when(repository.save(existingEntity)).thenReturn(existingEntity);

        processor.process(dto);

        verify(repository, times(2)).findByExternalId(400L);
        verify(repository, times(2)).save(any(CharacterEntity.class));
    }

    @Test
    void process_should_throw_after_max_retries() {
        var dto = new CharacterApiDto();
        dto.setId(500L);

        var entity = new CharacterEntity();

        when(repository.findByExternalId(500L)).thenReturn(Optional.empty());
        when(mapper.updateFromApi(null, dto)).thenReturn(entity);
        when(repository.save(entity))
                .thenThrow(new DataIntegrityViolationException("Duplicate key"));

        assertThrows(
                DataIntegrityViolationException.class,
                () -> processor.process(dto)
        );

        verify(repository, times(3)).findByExternalId(500L);
        verify(repository, times(3)).save(entity);
    }
}
