package com.innowise.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.orderservice.dto.OrderDTO;
import com.innowise.orderservice.kafka.OrderProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderEventService {

    private final OrderProducer orderProducer;
    private final ObjectMapper objectMapper;

    public void sendCreateOrderEvent(OrderDTO orderDTO) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("orderId", orderDTO.getId());
            payload.put("userId", orderDTO.getUserId());
            payload.put("timestamp", orderDTO.getCreationDate());

            String message = objectMapper.writeValueAsString(payload);
            orderProducer.createOrderEvent(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
