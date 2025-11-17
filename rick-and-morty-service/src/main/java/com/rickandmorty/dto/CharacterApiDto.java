package com.rickandmorty.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CharacterApiDto {

    @NotNull(message = "Character ID must not be null")
    @Positive(message = "Character ID must be positive")
    private Long id;

    @NotNull(message = "Character name must not be null")
    @Size(min = 1, max = 100, message = "Character name must be between 1 and 100 characters")
    private String name;

    @Size(max = 20, message = "Status must not exceed 20 characters")
    private String status;

    @Size(max = 50, message = "Species must not exceed 50 characters")
    private String species;

    @Size(max = 100, message = "Type must not exceed 100 characters")
    private String type;

    @Size(max = 20, message = "Gender must not exceed 20 characters")
    private String gender;

    private SimpleNamedResource origin;
    private SimpleNamedResource location;

    @Size(max = 255, message = "Image URL must not exceed 255 characters")
    private String image;

    private List<String> episode;

    @Size(max = 255, message = "API URL must not exceed 255 characters")
    private String url;

    private String created;

    public CharacterApiDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public SimpleNamedResource getOrigin() {
        return origin;
    }

    public void setOrigin(SimpleNamedResource origin) {
        this.origin = origin;
    }

    public SimpleNamedResource getLocation() {
        return location;
    }

    public void setLocation(SimpleNamedResource location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getEpisode() {
        return episode;
    }

    public void setEpisode(List<String> episode) {
        this.episode = episode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
