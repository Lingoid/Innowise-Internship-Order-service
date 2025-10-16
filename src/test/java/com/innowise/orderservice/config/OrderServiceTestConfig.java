package com.innowise.orderservice.config;

import com.innowise.orderservice.integration.UserRequest;
import com.innowise.orderservice.mapper.*;
import com.innowise.orderservice.repository.ItemRepository;
import com.innowise.orderservice.repository.OrderRepository;
import com.innowise.orderservice.service.ItemService;
import com.innowise.orderservice.service.OrderService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class OrderServiceTestConfig {

    @Bean
    public OrderMapper orderMapper() {
        return new OrderMapperImpl();
    }

    @Bean
    public OrderItemMapper orderItemMapper() {
        return new OrderItemMapperImpl();
    }

    @Bean
    public ItemMapper itemMapper() {
        return new ItemMapperImpl();
    }

    @Bean
    public ItemService itemService(ItemRepository itemRepository) {
        return new ItemService(itemRepository);
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepository, OrderMapper orderMapper,
                                     ItemService itemService, UserRequest userRequest,
                                     ItemMapper itemMapper) {
        return new OrderService(orderRepository, orderMapper, itemService, userRequest, itemMapper);
    }


}
