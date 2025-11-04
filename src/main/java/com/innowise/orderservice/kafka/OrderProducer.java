package com.innowise.orderservice.kafka;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    private final String topic;

    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate,
                         @Value("${spring.kafka.topics.create-order}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void createOrderEvent(String message) {
        kafkaTemplate.send(topic, message);

    }
}
