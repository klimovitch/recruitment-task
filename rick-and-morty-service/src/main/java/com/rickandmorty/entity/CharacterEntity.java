package com.rickandmorty.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "rm_character")
public class CharacterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "external_id", nullable = false)
    private Long externalId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 20)
    private String status;

    @Column(length = 50)
    private String species;

    @Column(length = 100)
    private String type;

    @Column(length = 20)
    private String gender;

    @Column(name = "origin_name", length = 100)
    private String originName;

    @Column(name = "origin_url", length = 255)
    private String originUrl;

    @Column(name = "location_name", length = 100)
    private String locationName;

    @Column(name = "location_url", length = 255)
    private String locationUrl;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "episodes_json", columnDefinition = "TEXT")
    private String episodesJson;

    @Column(name = "api_url", length = 255)
    private String apiUrl;

    @Column(name = "created_in_api")
    private LocalDateTime createdInApi;

    public CharacterEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
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

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationUrl() {
        return locationUrl;
    }

    public void setLocationUrl(String locationUrl) {
        this.locationUrl = locationUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEpisodesJson() {
        return episodesJson;
    }

    public void setEpisodesJson(String episodesJson) {
        this.episodesJson = episodesJson;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public LocalDateTime getCreatedInApi() {
        return createdInApi;
    }

    public void setCreatedInApi(LocalDateTime createdInApi) {
        this.createdInApi = createdInApi;
    }
}
