package com.rickandmorty.service;

import com.rickandmorty.dto.CharacterResponse;
import com.rickandmorty.mapper.CharacterResponseMapper;
import com.rickandmorty.repository.CharacterRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.rickandmorty.config.CacheConfig.CHARACTER_SEARCH_CACHE;

@Service
public class CharacterService {

    private final CharacterRepository repository;
    private final CharacterResponseMapper responseMapper;

    public CharacterService(CharacterRepository repository, CharacterResponseMapper responseMapper) {
        this.repository = repository;
        this.responseMapper = responseMapper;
    }

    @Cacheable(
            value = CHARACTER_SEARCH_CACHE,
            key = "#name + ':' + #pageable.pageNumber + ':' + #pageable.pageSize",
            condition = "#name != null && !#name.isBlank()"
    )
    public Page<CharacterResponse> findByName(String name, Pageable pageable) {
        if (name == null || name.isBlank()) {
            return Page.empty(pageable);
        }

        var normalizedName = name.trim();

        return repository.findByNameContainingIgnoreCase(normalizedName, pageable)
                         .map(responseMapper::toResponse);
    }
}
