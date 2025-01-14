package com.example.project2.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.project2.Entities.Message;

@Service
public class KafkaService {
    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    @Autowired
    private WebSocketService webSocketService;

    private static final String TOPIC = "message-topic";

    /**
     * Kafka Consumer
     * @param message The message that got send by the producer
     */
    @KafkaListener(topics=TOPIC, groupId = "message-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(Message message)
    {
        webSocketService.sendMessage(message);
    }

    /**
     * Kafka producer
     * @param message The message that the producer will send to consumers
     */
    public void send(Message message)
    {
        kafkaTemplate.send(TOPIC, message);
    }
}
