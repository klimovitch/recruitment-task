package com.rickandmorty.repository;

import com.rickandmorty.entity.CharacterEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CharacterRepositoryTest {

    @Container
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>("mariadb:10.5")
            .withDatabaseName("rickmorty_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariaDB::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDB::getUsername);
        registry.add("spring.datasource.password", mariaDB::getPassword);

        registry.add("spring.flyway.url", mariaDB::getJdbcUrl);
        registry.add("spring.flyway.user", mariaDB::getUsername);
        registry.add("spring.flyway.password", mariaDB::getPassword);
    }

    @Autowired
    private CharacterRepository repository;

    @Test
    void findByExternalId_should_return_entity_when_exists() {
        var entity = new CharacterEntity();
        entity.setExternalId(123L);
        entity.setName("Rick");

        repository.save(entity);

        var found = repository.findByExternalId(123L);

        assertTrue(found.isPresent());
        assertEquals("Rick", found.get().getName());
    }

    @Test
    void findByExternalId_should_return_empty_when_not_exists() {
        Optional<CharacterEntity> found = repository.findByExternalId(999L);
        assertTrue(found.isEmpty());
    }
}
