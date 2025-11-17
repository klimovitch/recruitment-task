package com.rickandmorty.service;

import com.rickandmorty.client.RickAndMortyApiClient;
import com.rickandmorty.dto.CharacterApiDto;
import com.rickandmorty.dto.CharacterPageResponse;
import com.rickandmorty.exception.InvalidApiResponseException;
import com.rickandmorty.messaging.CharacterImportProducer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.rickandmorty.config.CacheConfig.CHARACTER_SEARCH_CACHE;
import static java.util.stream.Stream.empty;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CharacterImportService {

    private static final Logger log = getLogger(CharacterImportService.class);

    private final RickAndMortyApiClient apiClient;
    private final CharacterImportProducer producer;
    @Value("${rickmorty.api.retry-attempts:3}")
    private int retryAttempts;

    public CharacterImportService(RickAndMortyApiClient apiClient,
                                  CharacterImportProducer producer) {
        this.apiClient = apiClient;
        this.producer = producer;
    }

    @CacheEvict(cacheNames = CHARACTER_SEARCH_CACHE, allEntries = true)
    public void importCharacters() {
        log.info("Starting Rick & Morty characters import");

        long start = System.currentTimeMillis();

        List<CharacterApiDto> characters = fetchAllPages()
                .flatMap(this::charactersOfPage)
                .toList();

        log.info("Fetched {} characters, sending to queue", characters.size());

        for (CharacterApiDto character : characters) {
            log.debug("Sending character {}", character.getId());
            producer.send(character);
        }

        long duration = System.currentTimeMillis() - start;
        log.info("Finished characters import in {} ms", duration);
    }

    private Stream<CharacterPageResponse> fetchAllPages() {
        var firstPage = fetchPageWithRetry(1);

        if (isEmpty(firstPage)) {
            log.warn("First page is empty, nothing to import");
            return Stream.empty();
        }

        int totalPages = getTotalPages(firstPage);
        log.info("Importing characters from {} pages", totalPages);

        return Stream.concat(
                Stream.of(firstPage),
                IntStream.rangeClosed(2, totalPages)
                         .mapToObj(this::fetchPageWithRetry)
                         .filter(Objects::nonNull)
        );
    }

    private CharacterPageResponse fetchPageWithRetry(int page) {
        for (int attempt = 1; attempt <= retryAttempts; attempt++) {
            try {
                return fetchPageOnce(page, attempt, retryAttempts);
            } catch (RuntimeException exception) {
                log.warn("Failed to fetch page {} on attempt {}/{}", page, attempt, retryAttempts, exception);

                if (attempt == retryAttempts) {
                    handlePageFailure(page, exception);
                    return null;
                }
            }
        }
        return null;
    }

    private CharacterPageResponse fetchPageOnce(int page, int attempt, int maxAttempts) {
        log.debug("Fetching page {} (attempt {}/{})", page, attempt, maxAttempts);

        var response = apiClient.getCharactersPage(page);

        int count = (response != null && response.getResults() != null)
                ? response.getResults().size()
                : 0;

        log.debug("Fetched page {} ({} characters)", page, count);
        return response;
    }

    private void handlePageFailure(int page, Exception exception) {
        if (page == 1) {
            throw new InvalidApiResponseException(
                    "Failed to fetch first page from Rick and Morty API after "
                            + retryAttempts + " attempts",
                    exception
            );
        }
        log.error("Giving up fetching page {} after {} attempts. Page will be skipped.",
                page, retryAttempts);
    }

    private Stream<CharacterApiDto> charactersOfPage(CharacterPageResponse page) {
        var results = page.getResults();
        return results == null ? empty() : results.stream();
    }

    private boolean isEmpty(CharacterPageResponse page) {
        return page == null
                || page.getResults() == null
                || page.getResults().isEmpty();
    }

    private int getTotalPages(CharacterPageResponse firstPage) {
        if (firstPage.getInfo() == null || firstPage.getInfo().getPages() <= 0) {
            throw new InvalidApiResponseException("Invalid API response: `info.pages` is missing or zero");
        }
        return firstPage.getInfo().getPages();
    }
}
