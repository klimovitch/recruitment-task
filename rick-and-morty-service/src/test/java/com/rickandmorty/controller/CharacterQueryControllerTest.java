package com.rickandmorty.controller;

import com.rickandmorty.dto.CharacterResponse;
import com.rickandmorty.service.CharacterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CharacterQueryControllerTest {
    @Mock
    private CharacterService characterService;

    @InjectMocks
    private CharacterQueryController controller;

    @Test
    void searchByName_should_return_ok_and_page_from_service() {
        var rick = new CharacterResponse(
                1L,
                100L,
                "Rick Sanchez",
                null, null, null, null,
                null, null, null, null,
                of(), null, null, null
        );

        Pageable pageable = PageRequest.of(0, 20);
        Page<CharacterResponse> page = new PageImpl<>(of(rick), pageable, 1);

        when(characterService.findByName(eq("Rick"), any(Pageable.class))).thenReturn(page);

        var response = controller.searchByName("Rick", 0, 20);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("Rick Sanchez", response.getBody().getContent().get(0).getName());

        verify(characterService).findByName(eq("Rick"), any(Pageable.class));
    }

    @Test
    void searchByName_should_return_ok_and_empty_page_when_service_returns_empty() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<CharacterResponse> emptyPage = Page.empty(pageable);

        when(characterService.findByName(eq("Unknown"), any(Pageable.class))).thenReturn(emptyPage);

        var response = controller.searchByName("Unknown", 0, 20);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getTotalElements());

        verify(characterService).findByName(eq("Unknown"), any(Pageable.class));
    }
}
