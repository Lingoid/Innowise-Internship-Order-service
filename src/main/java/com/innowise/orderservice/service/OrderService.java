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
import com.innowise.orderservice.util.OrderNotFoundException;
import com.innowise.orderservice.util.UserIdMismatchException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ItemService itemService;
    private final UserRequest userRequest;
    private final ItemMapper itemMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, ItemService itemService, UserRequest userRequest, ItemMapper itemMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.itemService = itemService;
        this.userRequest = userRequest;
        this.itemMapper = itemMapper;
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);

        List<Item> savedItems = orderDTO.getOrderItems().stream()
                .map(oi -> itemService.createItem(
                        Item.builder()
                                .name(oi.getItem().getName())
                                .price(oi.getItem().getPrice())
                                .build()))
                .toList();

        for (int i = 0; i < order.getOrderItems().size(); i++) {
            order.getOrderItems().get(i).setItem(savedItems.get(i));
            order.getOrderItems().get(i).setOrder(order);
        }

        Order savedOrder = orderRepository.save(order);

        OrderDTO result = orderMapper.fromEntity(savedOrder);
        result.setUserEmail(orderDTO.getUserEmail());
        return result;
    }

    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        OrderDTO orderDTO = orderMapper.fromEntity(order);

        UserInfoDTO userInfo = userRequest.getUserById(orderDTO.getUserId());

        orderDTO.setUserEmail(userInfo.getEmail());

        return new OrderResponseDTO(orderDTO, userInfo);
    }

    public List<OrderResponseDTO> getOrdersByIds(List<Long> ids) {
        List<OrderDTO> orders = orderRepository.findAllById(ids)
                .stream()
                .map(orderMapper::fromEntity)
                .toList();

        return orders.stream()
                .map(order -> {
                    UserInfoDTO userInfo = userRequest.getUserById(order.getUserId());
                    return new OrderResponseDTO(order, userInfo);
                })
                .toList();
    }

    public List<OrderResponseDTO> getOrdersByStatuses(List<String> statuses) {
        List<OrderDTO> orders = orderRepository.findByStatusIn(statuses)
                .stream()
                .map(orderMapper::fromEntity)
                .toList();

        return orders.stream()
                .map(order -> {
                    UserInfoDTO userInfo = userRequest.getUserById(order.getUserId());
                    order.setUserEmail(userInfo.getEmail());
                    return new OrderResponseDTO(order, userInfo);
                })
                .toList();
    }

    public boolean existsById(UserInfoDTO userInfoDTO, OrderDTO orderDTO){
        if (!userInfoDTO.getId().equals(orderDTO.getUserId())) {
            throw new UserIdMismatchException();
        }
        return true;
    }

    @Transactional
    public OrderResponseDTO updateOrder(Long id, OrderDTO orderDTO) {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        Order orderToUpdate = orderMapper.toEntity(orderDTO);
        orderToUpdate.setId(existingOrder.getId());

        List<Item> updatedItems = orderDTO.getOrderItems().stream()
                .map(oi -> {
                    Item itemEntity = itemMapper.toEntity(oi.getItem());
                    return itemService.createItem(itemEntity);
                })
                .toList();


        for (int i = 0; i < orderToUpdate.getOrderItems().size(); i++) {
            orderToUpdate.getOrderItems().get(i).setItem(updatedItems.get(i));
            orderToUpdate.getOrderItems().get(i).setOrder(orderToUpdate);
        }

        Order savedOrder = orderRepository.save(orderToUpdate);

        OrderDTO resultDto = orderMapper.fromEntity(savedOrder);
        UserInfoDTO userInfo = userRequest.getUserById(resultDto.getUserId());
        resultDto.setUserEmail(userInfo.getEmail());

        return new OrderResponseDTO(resultDto, userInfo);
    }

    @Transactional
    public void deleteOrder(Long id) {
        Optional.ofNullable(orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new));
        orderRepository.deleteById(id);
    }
}
