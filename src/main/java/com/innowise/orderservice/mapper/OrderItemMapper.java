package com.innowise.orderservice.mapper;

import com.innowise.orderservice.dto.OrderItemDTO;
import com.innowise.orderservice.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ItemMapper.class})
public interface OrderItemMapper {

    OrderItemDTO toDto(OrderItem orderItem);
    OrderItem toEntity(OrderItemDTO orderItemDTO);
}
