package com.rickandmorty.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickandmorty.dto.CharacterResponse;
import com.rickandmorty.entity.CharacterEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CharacterResponseMapperTest {

    private CharacterResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CharacterResponseMapper(new ObjectMapper());
    }

    @Test
    void toResponse_should_map_entity_to_response_with_episodes_happy_path() {
        var entity = new CharacterEntity();
        entity.setId(10L);
        entity.setExternalId(100L);
        entity.setName("Rick Sanchez");
        entity.setStatus("Alive");
        entity.setSpecies("Human");
        entity.setType("");
        entity.setGender("Male");
        entity.setOriginName("Earth");
        entity.setLocationName("Citadel of Ricks");
        entity.setImageUrl("https://image");
        entity.setApiUrl("https://api/rick/100");
        entity.setCreatedInApi(LocalDateTime.now());
        entity.setEpisodesJson("[\"https://episode1\", \"https://episode2\"]");

        var response = mapper.toResponse(entity);

        assertEquals(entity.getId(), response.getId());
        assertEquals(entity.getExternalId(), response.getExternalId());
        assertEquals(entity.getName(), response.getName());
        assertEquals(entity.getStatus(), response.getStatus());
        assertEquals(entity.getSpecies(), response.getSpecies());
        assertEquals(entity.getGender(), response.getGender());
        assertEquals(entity.getOriginName(), response.getOriginName());
        assertEquals(entity.getLocationName(), response.getLocationName());
        assertEquals(entity.getImageUrl(), response.getImageUrl());
        assertEquals(entity.getApiUrl(), response.getApiUrl());
        assertEquals(entity.getCreatedInApi(), response.getCreatedInApi());

        assertNotNull(response.getEpisodes());
        assertEquals(2, response.getEpisodes().size());
        assertTrue(response.getEpisodes().contains("https://episode1"));
        assertTrue(response.getEpisodes().contains("https://episode2"));
    }

    @Test
    void toResponse_should_return_empty_episodes_when_episodes_json_is_null() {
        CharacterEntity entity = new CharacterEntity();
        entity.setId(11L);
        entity.setName("Morty");
        entity.setEpisodesJson(null);

        var response = mapper.toResponse(entity);

        assertNotNull(response);
        assertNotNull(response.getEpisodes());
        assertTrue(response.getEpisodes().isEmpty());
    }
}
