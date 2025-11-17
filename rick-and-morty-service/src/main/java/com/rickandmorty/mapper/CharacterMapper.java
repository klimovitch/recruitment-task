package com.rickandmorty.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickandmorty.dto.CharacterApiDto;
import com.rickandmorty.dto.SimpleNamedResource;
import com.rickandmorty.entity.CharacterEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class CharacterMapper {

    private static final Logger log = LoggerFactory.getLogger(CharacterMapper.class);

    private final ObjectMapper objectMapper;

    public CharacterMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CharacterEntity updateFromApi(CharacterEntity entity, CharacterApiDto api) {
        if (api == null) {
            throw new IllegalArgumentException("API character must not be null");
        }

        if (entity == null) {
            entity = new CharacterEntity();
        }

        entity.setExternalId(api.getId());
        entity.setName(api.getName());
        entity.setStatus(api.getStatus());
        entity.setSpecies(api.getSpecies());
        entity.setType(api.getType());
        entity.setGender(api.getGender());
        applyOrigin(api.getOrigin(), entity);
        applyLocation(api.getLocation(), entity);
        entity.setEpisodesJson(toEpisodesJson(api.getEpisode()));
        entity.setImageUrl(api.getImage());
        entity.setApiUrl(api.getUrl());

        if (api.getCreated() != null) {
            OffsetDateTime odt = OffsetDateTime.parse(api.getCreated());
            entity.setCreatedInApi(odt.toLocalDateTime());
        }

        return entity;
    }

    private String toEpisodesJson(List<String> episodes) {
        if (episodes == null || episodes.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(episodes);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize episodes for persistence", e);
        }
    }

    private void applyOrigin(SimpleNamedResource origin, CharacterEntity entity) {
        if (origin == null) {
            entity.setOriginName(null);
            entity.setOriginUrl(null);
        } else {
            entity.setOriginName(origin.getName());
            entity.setOriginUrl(origin.getUrl());
        }
    }

    private void applyLocation(SimpleNamedResource location, CharacterEntity entity) {
        if (location == null) {
            entity.setLocationName(null);
            entity.setLocationUrl(null);
        } else {
            entity.setLocationName(location.getName());
            entity.setLocationUrl(location.getUrl());
        }
    }
}
