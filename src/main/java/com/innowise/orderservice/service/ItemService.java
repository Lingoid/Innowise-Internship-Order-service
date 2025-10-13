package com.innowise.orderservice.service;

import com.innowise.orderservice.model.Item;
import com.innowise.orderservice.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(Item item) {
        if (item.getId() != null) {
            Optional<Item> existingItem = itemRepository.findById(item.getId());
            if (existingItem.isPresent()) {
                Item updated = existingItem.get();
                updated.setName(item.getName());
                updated.setPrice(item.getPrice());
                return itemRepository.save(updated);
            }
        }
        return itemRepository.save(item);
    }

}
