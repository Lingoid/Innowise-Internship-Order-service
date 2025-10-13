package com.innowise.orderservice.integration;

import com.innowise.orderservice.config.UserRequestTestConfig;
import com.innowise.orderservice.dto.UserInfoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(UserRequestTestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRequestIntegrationTest {
    @Autowired
    private UserRequest userRequest;

    @Autowired
    @Qualifier("mockRest")
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        Mockito.reset(restTemplate);
    }

    @Test
    void getUserById_shouldReturnUserInfo() {
        Mockito.when(restTemplate.getForObject(
                        Mockito.anyString(),
                        Mockito.eq(UserInfoDTO.class),
                        Mockito.eq(1L)))
                .thenReturn(new UserInfoDTO(1L, "Max", "Bagel", LocalDate.now(), "test@example.com"));

        UserInfoDTO userInfo = userRequest.getUserById(1L);

        assertAll(() -> {
            assertEquals(1L, userInfo.getId());
            assertEquals("test@example.com", userInfo.getEmail());
        });
    }

    @Test
    void getUserByEmail_shouldReturnUserInfo() {
        Mockito.when(restTemplate.getForObject(
                        Mockito.anyString(),
                        Mockito.eq(UserInfoDTO.class),
                        Mockito.eq("test@example.com")))
                .thenReturn(new UserInfoDTO(1L, "Max", "Bagel", LocalDate.now(), "test@example.com"));

        UserInfoDTO userInfo = userRequest.getUserByEmail("test@example.com");

        assertAll(() -> {
            assertEquals(1L, userInfo.getId());
            assertEquals("test@example.com", userInfo.getEmail());
        });
    }

    @Test
    void getUserById_shouldThrowException_whenNotFound() {
        Mockito.when(restTemplate.getForObject(
                        Mockito.anyString(),
                        Mockito.eq(UserInfoDTO.class),
                        Mockito.eq(999L)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(HttpClientErrorException.class, () -> userRequest.getUserById(999L));
    }

    @Test
    void getUserByEmail_shouldThrowException_whenNotFound() {
        Mockito.when(restTemplate.getForObject(
                        Mockito.anyString(),
                        Mockito.eq(UserInfoDTO.class),
                        Mockito.eq("unknown@example.com")))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(HttpClientErrorException.class, () -> userRequest.getUserByEmail("unknown@example.com"));
    }
}
