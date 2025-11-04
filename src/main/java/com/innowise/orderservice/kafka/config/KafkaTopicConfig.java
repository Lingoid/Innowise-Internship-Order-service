package com.innowise.orderservice.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${spring.kafka.topics.create-order}")
    private String createOrderTopic;

    @Value("${spring.kafka.topics.create-payment}")
    private String createPaymentTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public KafkaAdmin.NewTopics topics() {
        return new KafkaAdmin.NewTopics(
                new NewTopic(createOrderTopic, 1, (short) 1),
                new NewTopic(createPaymentTopic, 1, (short) 1)
        );
    }
}
