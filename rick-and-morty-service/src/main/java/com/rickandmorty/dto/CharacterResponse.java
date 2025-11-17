package com.rickandmorty.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "CharacterResponse", description = "Character stored in database")
public class CharacterResponse implements Serializable {

    @Schema(description = "DB id")
    private Long id;
    @Schema(description = "External Rick&Morty API id")
    private Long externalId;
    @Schema(description = "The name of the character")
    private String name;
    @Schema(description = "The status of the character ('Alive', 'Dead' or 'unknown')")
    private String status;
    @Schema(description = "The species of the character")
    private String species;
    @Schema(description = "The type or subspecies of the character")
    private String type;
    @Schema(description = "The gender of the character ('Female', 'Male', 'Genderless' or 'unknown')")
    private String gender;
    @Schema(description = "Name origin location")
    private String originName;
    @Schema(description = "Link origin location")
    private String originUrl;
    @Schema(description = "Name character's last known location endpoint")
    private String locationName;
    @Schema(description = "Link to the character's last known location endpoint")
    private String locationUrl;
    @Schema(description = "List of episodes in which this character appeared")
    private List<String> episodes;
    @Schema(description = "Link to the character's image")
    private String imageUrl;
    @Schema(description = "Link to the character's own URL endpoint")
    private String apiUrl;
    @Schema(description = "Time at which the character was created in the Rick&Morty API")
    private LocalDateTime createdInApi;

    public CharacterResponse(Long id, Long externalId, String name, String status,
                             String species, String type, String gender,
                             String originName, String originUrl, String locationName, String locationUrl,
                             List<String> episodes, String imageUrl, String apiUrl, LocalDateTime createdInApi) {
        this.id = id;
        this.externalId = externalId;
        this.name = name;
        this.status = status;
        this.species = species;
        this.type = type;
        this.gender = gender;
        this.originName = originName;
        this.originUrl = originUrl;
        this.locationName = locationName;
        this.locationUrl = locationUrl;
        this.episodes = episodes;
        this.imageUrl = imageUrl;
        this.apiUrl = apiUrl;
        this.createdInApi = createdInApi;
    }

    public Long getId() {
        return id;
    }

    public Long getExternalId() {
        return externalId;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getSpecies() {
        return species;
    }

    public String getType() {
        return type;
    }

    public String getGender() {
        return gender;
    }

    public String getOriginName() {
        return originName;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocationUrl() {
        return locationUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getEpisodes() {
        return episodes;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public LocalDateTime getCreatedInApi() {
        return createdInApi;
    }
}
