package com.rickandmorty.client;

import com.rickandmorty.dto.CharacterPageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RickAndMortyApiClient {

    private final RestClient restClient;
    private final String characterPath;
    private final String pageParam;

    public RickAndMortyApiClient(RestClient rickAndMortyRestClient,
                                 @Value("${rickmorty.api.character-path}") String characterPath,
                                 @Value("${rickmorty.api.page-param}") String pageParam) {
        this.restClient = rickAndMortyRestClient;
        this.characterPath = characterPath;
        this.pageParam = pageParam;
    }

    public CharacterPageResponse getCharactersPage(int page) {
        return restClient.get()
                         .uri(uriBuilder -> uriBuilder
                                 .path(characterPath)
                                 .queryParam(pageParam, page)
                                 .build())
                         .retrieve()
                         .body(CharacterPageResponse.class);
    }
}
