package com.rickandmorty.controller;

import com.rickandmorty.service.CharacterImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping("/api/characters")
public class CharacterImportController {

    private final CharacterImportService importService;

    public CharacterImportController(CharacterImportService importService) {
        this.importService = importService;
    }

    @Operation(
            summary = "Import characters from external API",
            description = "Triggers asynchronous import of all characters from the Rick & Morty API. Returns 202 on start."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Import started"),
            @ApiResponse(responseCode = "502", description = "External API error"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @PostMapping("/import")
    public ResponseEntity<Void> importCharacters() {
        CompletableFuture.runAsync(importService::importCharacters);
        return accepted().build();
    }
}
