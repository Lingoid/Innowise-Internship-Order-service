package com.innowise.orderservice.config;

import com.innowise.orderservice.integration.UserRequest;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class UserRequestTestConfig {

    @Bean(name = "mockRest")
    public RestTemplate restTemplate() {
        return Mockito.mock(RestTemplate.class);
    }

    @Bean
    public UserRequest userRequest(@Value("${userService.url}") String userServiceUrl,
                                   @Qualifier("mockRest") RestTemplate restTemplate) {
        return new UserRequest(restTemplate, userServiceUrl);
    }
}
