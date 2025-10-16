package com.innowise.orderservice.service;

import com.innowise.orderservice.dto.OrderDTO;
import com.innowise.orderservice.dto.OrderResponseDTO;
import com.innowise.orderservice.dto.UserInfoDTO;
import com.innowise.orderservice.integration.UserRequest;
import com.innowise.orderservice.mapper.ItemMapper;
import com.innowise.orderservice.mapper.OrderMapper;
import com.innowise.orderservice.model.Item;
import com.innowise.orderservice.model.Order;
import com.innowise.orderservice.repository.OrderRepository;
import com.innowise.orderservice.testdata.TestDataFactory;
import com.innowise.orderservice.util.OrderNotFoundException;
import com.innowise.orderservice.util.UserIdMismatchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private OrderMapper orderMapper;
    @Mock private ItemService itemService;
    @Mock private UserRequest userRequest;
    @Mock private ItemMapper itemMapper;

    @InjectMocks private OrderService orderService;

    private final OrderDTO orderDTO = TestDataFactory.getTestOrderDTO(1L, "test@example.com");
    private final UserInfoDTO userInfo = TestDataFactory.getTestUser();
    private final Order orderEntity = TestDataFactory.buildOrderEntityFromDTO(orderDTO);

    @Test
    void createOrder_shouldReturnSavedOrder() {
        when(orderMapper.toEntity(orderDTO)).thenReturn(orderEntity);
        when(itemService.createItem(any(Item.class))).thenAnswer(i -> i.getArgument(0));
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(orderMapper.fromEntity(orderEntity)).thenReturn(orderDTO);

        OrderDTO result = orderService.createOrder(orderDTO);

        assertEquals(orderDTO.getUserEmail(), result.getUserEmail());
        verify(orderRepository).save(orderEntity);
    }

    @Test
    void getOrderById_shouldReturnOrderResponse() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));
        when(orderMapper.fromEntity(orderEntity)).thenReturn(orderDTO);
        when(userRequest.getUserById(anyLong())).thenReturn(userInfo);

        OrderResponseDTO response = orderService.getOrderById(1L);

        assertEquals(orderDTO.getUserEmail(), response.getOrder().getUserEmail());
        assertEquals(userInfo.getEmail(), response.getUser().getEmail());
    }

    @Test
    void getOrderById_shouldThrowException_whenNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(999L));
    }

    @Test
    void existsById_shouldValidateUser() {
        OrderDTO dto = orderDTO;
        assertTrue(orderService.existsById(userInfo, dto));
        userInfo.setId(999L);
        assertThrows(UserIdMismatchException.class, () -> orderService.existsById(userInfo, dto));
    }

    @Test
    void updateOrder_shouldReturnUpdatedOrderResponse() {
        Order orderToSave = orderEntity;

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));
        when(orderMapper.toEntity(orderDTO)).thenReturn(orderToSave);
        when(itemMapper.toEntity(any())).thenAnswer(i -> new Item());
        when(itemService.createItem(any())).thenAnswer(i -> i.getArgument(0));
        when(orderRepository.save(orderToSave)).thenReturn(orderToSave);
        when(orderMapper.fromEntity(orderToSave)).thenReturn(orderDTO);
        when(userRequest.getUserById(anyLong())).thenReturn(userInfo);

        OrderResponseDTO response = orderService.updateOrder(1L, orderDTO);

        assertEquals(orderDTO.getUserEmail(), response.getOrder().getUserEmail());
        assertEquals(userInfo.getEmail(), response.getUser().getEmail());
    }

    @Test
    void deleteOrder_shouldCallRepositoryDeleteOrThrow() {
        Order existingOrder = orderEntity;
        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        orderService.deleteOrder(1L);
        verify(orderRepository).delete(existingOrder);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(999L));
    }
}
