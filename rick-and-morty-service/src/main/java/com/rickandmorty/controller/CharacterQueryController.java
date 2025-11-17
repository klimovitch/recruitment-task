package com.rickandmorty.controller;

import com.rickandmorty.dto.CharacterResponse;
import com.rickandmorty.service.CharacterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/characters")
public class CharacterQueryController {

    private final CharacterService characterService;

    public CharacterQueryController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @Operation(
            summary = "Search characters by name",
            description = "Returns a paginated list of DB stored characters. Names partially/fully match the provided value."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Characters found"),
    })
    @GetMapping("/search")
    public ResponseEntity<Page<CharacterResponse>> searchByName(
            @Parameter(description = "Name to search for", required = true)
            @RequestParam("name") String name,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(value = "size", defaultValue = "20") int size) {

        var pageable = PageRequest.of(page, size);
        var result = characterService.findByName(name, pageable);
        return ok(result);
    }
}
