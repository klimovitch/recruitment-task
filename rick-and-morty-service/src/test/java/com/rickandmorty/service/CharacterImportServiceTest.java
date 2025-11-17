package com.rickandmorty.service;

import com.rickandmorty.client.RickAndMortyApiClient;
import com.rickandmorty.dto.CharacterApiDto;
import com.rickandmorty.dto.CharacterPageResponse;
import com.rickandmorty.exception.InvalidApiResponseException;
import com.rickandmorty.messaging.CharacterImportProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacterImportServiceTest {

    private static final int RETRY_ATTEMPTS = 3;

    @Mock
    private RickAndMortyApiClient apiClient;

    @Mock
    private CharacterImportProducer producer;

    @InjectMocks
    private CharacterImportService service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "retryAttempts", RETRY_ATTEMPTS);
    }

    private CharacterPageResponse pageWithCharacters(int totalPages, long... ids) {
        var characters = Arrays.stream(ids)
                               .mapToObj(id -> {
                                   CharacterApiDto dto = new CharacterApiDto();
                                   dto.setId(id);
                                   return dto;
                               }).toList();

        var info = new CharacterPageResponse.Info(0, totalPages, null, null);
        return new CharacterPageResponse(info, characters);
    }

    private CharacterPageResponse emptyPage(int totalPages) {
        var info = new CharacterPageResponse.Info(0, totalPages, null, null);
        return new CharacterPageResponse(info, List.of());
    }

    @Test
    void importCharacters_should_fetch_all_pages_and_send_all_characters() {
        var page1 = pageWithCharacters(3, 1L, 2L);
        var page2 = pageWithCharacters(3, 3L);
        var page3 = pageWithCharacters(3, 4L, 5L);

        when(apiClient.getCharactersPage(1)).thenReturn(page1);
        when(apiClient.getCharactersPage(2)).thenReturn(page2);
        when(apiClient.getCharactersPage(3)).thenReturn(page3);

        service.importCharacters();

        verify(apiClient).getCharactersPage(1);
        verify(apiClient).getCharactersPage(2);
        verify(apiClient).getCharactersPage(3);
        verifyNoMoreInteractions(apiClient);

        verify(producer, times(5)).send(any(CharacterApiDto.class));
        verifyNoMoreInteractions(producer);
    }

    @Test
    void importCharacters_should_do_nothing_when_first_page_is_empty() {
        var emptyFirstPage = emptyPage(3);

        when(apiClient.getCharactersPage(1)).thenReturn(emptyFirstPage);

        service.importCharacters();

        verify(apiClient).getCharactersPage(1);
        verifyNoMoreInteractions(apiClient);
        verifyNoInteractions(producer);
    }

    @Test
    void importCharacters_should_throw_when_total_pages_invalid() {
        var dto = new CharacterApiDto();
        dto.setId(1L);

        var invalidFirstPage = new CharacterPageResponse(null, List.of(dto));

        when(apiClient.getCharactersPage(1)).thenReturn(invalidFirstPage);

        assertThrows(InvalidApiResponseException.class, () -> service.importCharacters());

        verify(apiClient).getCharactersPage(1);
        verifyNoMoreInteractions(apiClient);
        verifyNoInteractions(producer);
    }

    @Test
    void importCharacters_should_throw_when_first_page_fails_after_all_retries() {
        when(apiClient.getCharactersPage(1)).thenThrow(new RuntimeException("API down"));

        assertThrows(InvalidApiResponseException.class, () -> service.importCharacters());

        verify(apiClient, times(RETRY_ATTEMPTS)).getCharactersPage(1);
        verifyNoMoreInteractions(apiClient);
        verifyNoInteractions(producer);
    }

    @Test
    void importCharacters_should_skip_page_when_non_first_page_fails_after_all_retries() {
        var page1 = pageWithCharacters(3, 1L, 2L);
        var page3 = pageWithCharacters(3, 3L, 4L);


        when(apiClient.getCharactersPage(1)).thenReturn(page1);
        when(apiClient.getCharactersPage(2)).thenThrow(new RuntimeException("Page 2 error"));
        when(apiClient.getCharactersPage(3)).thenReturn(page3);


        service.importCharacters();

        verify(apiClient, times(1)).getCharactersPage(1);
        verify(apiClient, times(3)).getCharactersPage(2);
        verify(apiClient, times(1)).getCharactersPage(3);
        verifyNoMoreInteractions(apiClient);

        verify(producer, times(4)).send(any(CharacterApiDto.class));
        verifyNoMoreInteractions(producer);
    }

    @Test
    void importCharacters_should_handle_page_with_null_results_as_empty() {
        var firstPage = pageWithCharacters(2, 1L);

        var secondPage = new CharacterPageResponse(
                new CharacterPageResponse.Info(0, 2, null, null), null);

        when(apiClient.getCharactersPage(1)).thenReturn(firstPage);
        when(apiClient.getCharactersPage(2)).thenReturn(secondPage);

        service.importCharacters();

        verify(apiClient).getCharactersPage(1);
        verify(apiClient).getCharactersPage(2);
        verifyNoMoreInteractions(apiClient);

        verify(producer, times(1)).send(any(CharacterApiDto.class));
        verifyNoMoreInteractions(producer);
    }
}
