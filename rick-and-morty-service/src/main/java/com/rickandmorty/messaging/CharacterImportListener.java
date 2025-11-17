package com.rickandmorty.messaging;

import com.rickandmorty.dto.CharacterApiDto;
import com.rickandmorty.service.CharacterImportProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CharacterImportListener {

    private final CharacterImportProcessor processor;

    public CharacterImportListener(CharacterImportProcessor processor) {
        this.processor = processor;
    }

    @RabbitListener(queues = "${rickmorty.messaging.character-import-queue}")
    public void handleCharacter(CharacterApiDto dto) {
        processor.process(dto);
    }
}
