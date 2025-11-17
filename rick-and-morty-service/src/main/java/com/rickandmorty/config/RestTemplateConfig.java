package com.rickandmorty.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestClient rickAndMortyRestClient(@Value("${rickmorty.api.base-url}") String baseUrl) {
        return RestClient.builder()
                         .baseUrl(baseUrl)
                         .build();
    }
}
