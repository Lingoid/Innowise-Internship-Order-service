package com.innowise.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponseDTO {

    private OrderDTO order;
    private UserInfoDTO user;
}
