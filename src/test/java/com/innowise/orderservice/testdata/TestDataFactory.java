package com.innowise.orderservice.testdata;

import com.innowise.orderservice.dto.ItemDTO;
import com.innowise.orderservice.dto.OrderDTO;
import com.innowise.orderservice.dto.OrderItemDTO;
import com.innowise.orderservice.dto.UserInfoDTO;
import com.innowise.orderservice.model.Item;
import com.innowise.orderservice.model.Order;
import com.innowise.orderservice.model.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestDataFactory {

    public static UserInfoDTO getTestUser() {
        UserInfoDTO user = new UserInfoDTO();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Max");
        user.setSurname("Bagel");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        return user;
    }

    public static OrderDTO getTestOrderDTO(Long userId, String userEmail) {
        ItemDTO itemDTO = new ItemDTO(null, "Laptop", new BigDecimal("1000.00"));
        OrderItemDTO orderItemDTO = new OrderItemDTO(null, itemDTO, 2);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(userId);
        orderDTO.setUserEmail(userEmail);
        orderDTO.setOrderItems(List.of(orderItemDTO));
        orderDTO.setStatus("CREATED");

        return orderDTO;
    }

    public static Order buildOrderEntityFromDTO(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderItems(new ArrayList<>());
        orderDTO.getOrderItems().forEach(oi -> {
            OrderItem oiEntity = new OrderItem();
            oiEntity.setItem(new Item());
            order.getOrderItems().add(oiEntity);
        });
        return order;
    }

    public static Item buildItem(Long id, String name, String price) {
        return new Item(id, name, new BigDecimal(price), new ArrayList<>());
    }
}
