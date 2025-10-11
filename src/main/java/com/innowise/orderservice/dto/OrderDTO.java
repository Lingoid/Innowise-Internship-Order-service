package com.innowise.orderservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {

    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Order status shouldn't be empty")
    private String status;

    @Email(message = "Invalid email format")
    @NotBlank(message = "User email is required")
    private String userEmail;

    private LocalDateTime creationDate;

    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemDTO> orderItems;
}
