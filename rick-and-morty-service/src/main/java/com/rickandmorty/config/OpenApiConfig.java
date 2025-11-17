package com.rickandmorty.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Rick and Morty Service",
                description = "Service for importing and querying Rick and Morty characters",
                version = "1.0.0"
        )
)
public class OpenApiConfig {
}
