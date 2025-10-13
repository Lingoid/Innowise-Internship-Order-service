package com.innowise.orderservice.mapper;

import com.innowise.orderservice.dto.ItemDTO;
import com.innowise.orderservice.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {

    ItemDTO fromEntity(Item item);
    Item toEntity(ItemDTO itemDTO);
}
