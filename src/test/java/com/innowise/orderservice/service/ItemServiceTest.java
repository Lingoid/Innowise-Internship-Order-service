package com.innowise.orderservice.service;

import com.innowise.orderservice.model.Item;
import com.innowise.orderservice.repository.ItemRepository;
import com.innowise.orderservice.testdata.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void createItem_shouldSaveNewItem() {
        Item item = TestDataFactory.buildItem(null, "Laptop", "1000.00");
        when(itemRepository.save(item)).thenReturn(item);

        Item result = itemService.createItem(item);

        assertEquals(item, result);
        verify(itemRepository).save(item);
    }

    @Test
    void createItem_shouldUpdateExistingItem_whenIdExists() {
        Item existingItem = TestDataFactory.buildItem(1L, "OldLaptop", "500.00");
        Item updateItem = TestDataFactory.buildItem(1L, "Laptop", "1000.00");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(existingItem)).thenReturn(existingItem);

        Item result = itemService.createItem(updateItem);

        assertEquals("Laptop", result.getName());
        assertEquals(updateItem.getPrice(), result.getPrice());
        verify(itemRepository).save(existingItem);
    }

    @Test
    void createItem_shouldSaveNew_whenIdDoesNotExist() {
        Item item = TestDataFactory.buildItem(999L, "Laptop", "1000.00");

        when(itemRepository.findById(999L)).thenReturn(Optional.empty());
        when(itemRepository.save(item)).thenReturn(item);

        Item result = itemService.createItem(item);

        assertEquals(item, result);
        verify(itemRepository).save(item);
    }
}
