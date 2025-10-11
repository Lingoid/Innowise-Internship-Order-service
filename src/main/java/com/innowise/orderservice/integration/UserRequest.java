package com.innowise.orderservice.integration;

import com.innowise.orderservice.dto.UserInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserRequest {

    private final RestTemplate restTemplate;
    private final String userServiceUrl;

    public UserRequest(RestTemplate restTemplate, @Value("${userService.url}") String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }

    public UserInfoDTO getUserByEmail(String email) {
        return restTemplate.getForObject(userServiceUrl + "/email/{email}", UserInfoDTO.class, email);
    }

    public UserInfoDTO getUserById(Long userId) {
        return restTemplate.getForObject(userServiceUrl + "/{id}", UserInfoDTO.class, userId);
    }
}
