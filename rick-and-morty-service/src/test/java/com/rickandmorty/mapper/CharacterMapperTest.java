package com.rickandmorty.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickandmorty.dto.CharacterApiDto;
import com.rickandmorty.dto.SimpleNamedResource;
import com.rickandmorty.entity.CharacterEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CharacterMapperTest {

    private CharacterMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CharacterMapper(new ObjectMapper());
    }

    @Test
    void updateFromApi_should_create_new_entity_from_api_happy_path() {
        var api = buildFullApiDto();

        var result = mapper.updateFromApi(null, api);

        assertNotNull(result);
        assertNull(result.getId(), "id must be null for new entity");
        assertEquals(api.getId(), result.getExternalId());
        assertEquals(api.getName(), result.getName());
        assertEquals(api.getStatus(), result.getStatus());
        assertEquals(api.getSpecies(), result.getSpecies());
        assertEquals(api.getType(), result.getType());
        assertEquals(api.getGender(), result.getGender());
        assertEquals(api.getOrigin().getName(), result.getOriginName());
        assertEquals(api.getOrigin().getUrl(), result.getOriginUrl());
        assertEquals(api.getLocation().getName(), result.getLocationName());
        assertEquals(api.getLocation().getUrl(), result.getLocationUrl());
        assertEquals(api.getImage(), result.getImageUrl());
        assertEquals(api.getUrl(), result.getApiUrl());
        assertNotNull(result.getCreatedInApi());
        assertNotNull(result.getEpisodesJson());
        assertTrue(result.getEpisodesJson().contains("https://episode1"));
    }

    @Test
    void updateFromApi_should_keep_existing_id_when_updating_entity() {
        var api = buildFullApiDto();
        var existing = new CharacterEntity();
        existing.setId(42L);
        existing.setExternalId(999L);

        var result = mapper.updateFromApi(existing, api);

        assertSame(existing, result);
        assertEquals(42L, result.getId());
        assertEquals(api.getId(), result.getExternalId());
    }

    @Test
    void updateFromApi_should_throw_exception_when_api_is_null() {
        var existing = new CharacterEntity();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.updateFromApi(existing, null)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("api"));
    }

    @Test
    void updateFromApi_should_handle_null_origin_and_location_without_npe() {
        var api = new CharacterApiDto();
        api.setId(1L);
        api.setName("No Origin Character");
        api.setEpisode(List.of("https://episode1"));

        api.setOrigin(null);
        api.setLocation(null);

        var result = mapper.updateFromApi(null, api);

        assertNotNull(result);
        assertEquals("No Origin Character", result.getName());
        assertNull(result.getOriginName());
        assertNull(result.getOriginUrl());
        assertNull(result.getLocationName());
        assertNull(result.getLocationUrl());
    }

    private CharacterApiDto buildFullApiDto() {
        var api = new CharacterApiDto();
        api.setId(100L);
        api.setName("Rick Sanchez");
        api.setStatus("Alive");
        api.setSpecies("Human");
        api.setType("");
        api.setGender("Male");

        var origin = new SimpleNamedResource();
        origin.setName("Earth");
        origin.setUrl("https://origin/earth");
        api.setOrigin(origin);

        var location = new SimpleNamedResource();
        location.setName("Citadel of Ricks");
        location.setUrl("https://location/citadel");
        api.setLocation(location);

        api.setImage("https://image");
        api.setUrl("https://api/rick/100");
        api.setCreated("2025-11-16T10:15:30+01:00");
        api.setEpisode(List.of("https://episode1", "https://episode2"));

        return api;
    }
}
