package com.rickandmorty.controller;

import com.rickandmorty.service.CharacterImportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CharacterImportControllerTest {

    @Mock
    private CharacterImportService importService;

    @InjectMocks
    private CharacterImportController controller;

    @Test
    void importCharacters_should_trigger_service_and_return_202() {
        var response = controller.importCharacters();

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNull(response.getBody());

        verify(importService, timeout(2000)).importCharacters();
    }
}
