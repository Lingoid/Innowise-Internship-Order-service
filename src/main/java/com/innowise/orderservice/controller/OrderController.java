package com.innowise.orderservice.controller;

import com.innowise.orderservice.dto.OrderDTO;
import com.innowise.orderservice.dto.OrderResponseDTO;
import com.innowise.orderservice.dto.UserInfoDTO;
import com.innowise.orderservice.integration.UserRequest;
import com.innowise.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRequest userRequest;

    public OrderController(OrderService orderService, UserRequest userRequest) {
        this.orderService = orderService;
        this.userRequest = userRequest;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        UserInfoDTO userInfo = userRequest.getUserByEmail(orderDTO.getUserEmail());
        orderService.existsById(userInfo, orderDTO);
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new OrderResponseDTO(createdOrder, userInfo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByIds(@RequestParam List<Long> ids) {
        List<OrderResponseDTO> responseList = orderService.getOrdersByIds(ids);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/status")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatuses(@RequestParam List<String> statuses) {
        List<OrderResponseDTO> responseList = orderService.getOrdersByStatuses(statuses);
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Long id,
                                                        @Valid @RequestBody OrderDTO orderDTO) {
        OrderResponseDTO response = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }


}
