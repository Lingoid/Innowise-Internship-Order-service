package com.innowise.orderservice.mapper;

import com.innowise.orderservice.dto.OrderDTO;
import com.innowise.orderservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = OrderItemMapper.class)
public interface OrderMapper {

    OrderDTO fromEntity(Order order);
    Order toEntity(OrderDTO itemDTO);
}
