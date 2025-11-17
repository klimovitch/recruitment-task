package com.rickandmorty.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickandmorty.dto.CharacterResponse;
import com.rickandmorty.entity.CharacterEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CharacterResponseMapper {

    private static final Logger log = LoggerFactory.getLogger(CharacterResponseMapper.class);

    private final ObjectMapper objectMapper;

    public CharacterResponseMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CharacterResponse toResponse(CharacterEntity e) {
        return new CharacterResponse(
                e.getId(),
                e.getExternalId(),
                e.getName(),
                e.getStatus(),
                e.getSpecies(),
                e.getType(),
                e.getGender(),
                e.getOriginName(),
                e.getOriginUrl(),
                e.getLocationName(),
                e.getLocationUrl(),
                fromEpisodesJson(e.getEpisodesJson()),
                e.getImageUrl(),
                e.getApiUrl(),
                e.getCreatedInApi()
        );
    }

    private List<String> fromEpisodesJson(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            log.warn("Failed to deserialize episodesJson, returning empty list", e);
            return List.of();
        }
    }
}
