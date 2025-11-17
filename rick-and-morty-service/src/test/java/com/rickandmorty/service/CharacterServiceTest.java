package com.rickandmorty.service;

import com.rickandmorty.dto.CharacterResponse;
import com.rickandmorty.entity.CharacterEntity;
import com.rickandmorty.mapper.CharacterResponseMapper;
import com.rickandmorty.repository.CharacterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @Mock
    private CharacterRepository repository;
    @Mock
    private CharacterResponseMapper mapper;

    @InjectMocks
    private CharacterService service;

    @Test
    void findByName_should_return_characters_when_found() {
        CharacterEntity rick = new CharacterEntity();
        rick.setId(2L);
        rick.setExternalId(200L);
        rick.setName("Rick Sanchez");

        var rickResponse =
                new CharacterResponse(
                        2L,
                        200L,
                        "Rick Sanchez",
                        null, null, null, null,
                        null, null,null, null,
                        of(), null, null, null
                );

        Pageable pageable = PageRequest.of(0, 20);
        Page<CharacterEntity> entityPage = new PageImpl<>(of(rick), pageable, 1);

        when(repository.findByNameContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(entityPage);
        when(mapper.toResponse(rick)).thenReturn(rickResponse);

        Page<CharacterResponse> result = service.findByName("rick", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Rick Sanchez", result.getContent().get(0).getName());
    }

    @Test
    void findByName_should_return_empty_page_when_nothing_found() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<CharacterEntity> emptyPage = Page.empty(pageable);

        when(repository.findByNameContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(emptyPage);

        var result = service.findByName("unknown", pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }
}
