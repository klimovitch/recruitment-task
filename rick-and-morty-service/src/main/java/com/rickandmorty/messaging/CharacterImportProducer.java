package com.rickandmorty.messaging;

import com.rickandmorty.dto.CharacterApiDto;

public interface CharacterImportProducer {

    void send(CharacterApiDto character);
}
