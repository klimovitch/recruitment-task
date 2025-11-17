package com.rickandmorty.messaging;

import com.rickandmorty.dto.CharacterApiDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class RabbitCharacterImportProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private final String queueName = "test-queue";

    private RabbitCharacterImportProducer producer;

    @BeforeEach
    void setUp() {
        producer = new RabbitCharacterImportProducer(rabbitTemplate, queueName);
    }

    @Test
    void send_should_publish_message_when_character_not_null() {
        var dto = new CharacterApiDto();
        dto.setId(123L);
        dto.setName("Rick Sanchez");

        producer.send(dto);

        verify(rabbitTemplate).convertAndSend(queueName, dto);
    }

    @Test
    void send_should_not_publish_when_character_is_null() {
        producer.send(null);

        verifyNoInteractions(rabbitTemplate);
    }
}
