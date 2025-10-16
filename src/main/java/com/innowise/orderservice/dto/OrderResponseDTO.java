package com.innowise.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponseDTO {

    private OrderDTO order;
    private UserInfoDTO user;
}
