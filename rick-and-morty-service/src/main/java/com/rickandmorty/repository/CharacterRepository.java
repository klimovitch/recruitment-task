package com.rickandmorty.repository;

import com.rickandmorty.entity.CharacterEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<CharacterEntity, Long> {

    Optional<CharacterEntity> findByExternalId(Long externalId);

    Page<CharacterEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
