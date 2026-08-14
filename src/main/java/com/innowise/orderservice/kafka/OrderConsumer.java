package com.innowise.orderservice.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @KafkaListener(topics = "${spring.kafka.topics.create-payment}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleCreatePayment(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);

            Long orderId = Long.parseLong(jsonNode.get("orderId").asText());
            String status = jsonNode.get("status").asText();

            orderService.updateOrderStatus(orderId, status);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
