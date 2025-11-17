package com.rickandmorty.messaging;

import com.rickandmorty.dto.CharacterApiDto;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RabbitCharacterImportProducer implements CharacterImportProducer {

    private static final Logger log = getLogger(RabbitCharacterImportProducer.class);

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;

    public RabbitCharacterImportProducer(
            RabbitTemplate rabbitTemplate,
            @Value("${rickmorty.messaging.character-import-queue}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    @Override
    public void send(CharacterApiDto character) {
        if (character == null) {
            log.warn("Attempted to send null character to queue {}, skipping", queueName);
            return;
        }

        log.debug("Sending character {} to queue {}", character.getId(), queueName);
        rabbitTemplate.convertAndSend(queueName, character);
    }
}
