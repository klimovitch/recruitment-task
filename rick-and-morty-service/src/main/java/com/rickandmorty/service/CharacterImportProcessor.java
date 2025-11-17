package com.rickandmorty.service;

import com.rickandmorty.dto.CharacterApiDto;
import com.rickandmorty.mapper.CharacterMapper;
import com.rickandmorty.repository.CharacterRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@Validated
public class CharacterImportProcessor {

    private static final Logger log = getLogger(CharacterImportProcessor.class);
    private static final int MAX_RETRY_ATTEMPTS = 3;

    private final CharacterRepository repository;
    private final CharacterMapper mapper;

    public CharacterImportProcessor(CharacterRepository repository, CharacterMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public void process(@Valid CharacterApiDto dto) {
        if (dto == null) {
            log.warn("Received null dto, skipping");
            return;
        }

        log.debug("Processing character externalId={}", dto.getId());

        processWithRetry(dto, 1);
    }

    private void processWithRetry(CharacterApiDto dto, int attempt) {
        try {
            var existing = repository.findByExternalId(dto.getId()).orElse(null);
            var updated = mapper.updateFromApi(existing, dto);
            var saved = repository.save(updated);

            log.debug("Saved character externalId={} → entity id={}", dto.getId(), saved.getId());

        } catch (DataIntegrityViolationException e) {
            if (attempt >= MAX_RETRY_ATTEMPTS) {
                log.error("Failed to save character externalId={} after {} attempts",
                         dto.getId(), MAX_RETRY_ATTEMPTS, e);
                throw e;
            }

            log.warn("Duplicate key detected for externalId={}, retrying (attempt {}/{})",
                    dto.getId(), attempt, MAX_RETRY_ATTEMPTS);

            processWithRetry(dto, attempt + 1);
        }
    }
}
