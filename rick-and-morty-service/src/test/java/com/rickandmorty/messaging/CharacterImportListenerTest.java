package com.rickandmorty.messaging;

import com.rickandmorty.dto.CharacterApiDto;
import com.rickandmorty.service.CharacterImportProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CharacterImportListenerTest {

    @Mock
    private CharacterImportProcessor processor;

    @InjectMocks
    private CharacterImportListener listener;

    @Test
    void handleCharacter_should_delegate_to_processor() {
        var dto = new CharacterApiDto();
        dto.setId(123L);
        dto.setName("Rick Sanchez");

        listener.handleCharacter(dto);

        verify(processor).process(dto);
    }

    @Test
    void handleCharacter_should_propagate_exception_from_processor() {
        var dto = new CharacterApiDto();
        dto.setId(123L);

        var expected = new RuntimeException("processing failed");

        doThrow(expected).when(processor).process(dto);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> listener.handleCharacter(dto)
        );

        assertEquals("processing failed", thrown.getMessage());
    }
}
