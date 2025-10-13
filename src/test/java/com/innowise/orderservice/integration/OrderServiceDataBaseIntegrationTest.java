package com.innowise.orderservice.integration;


import com.innowise.orderservice.config.OrderServiceTestConfig;
import com.innowise.orderservice.dto.*;
import com.innowise.orderservice.repository.ItemRepository;
import com.innowise.orderservice.repository.OrderRepository;
import com.innowise.orderservice.service.OrderService;
import com.innowise.orderservice.testdata.TestDataFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.math.BigDecimal;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(OrderServiceTestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderServiceDataBaseIntegrationTest extends AbstractBaseIntegrationTest{

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;

    @MockitoBean
    private UserRequest userRequest;

    private UserInfoDTO testUser;

    @BeforeEach
    void cleanDatabase() {
        testUser = TestDataFactory.getTestUser();
        when(userRequest.getUserById(testUser.getId())).thenReturn(testUser);
        when(userRequest.getUserByEmail(testUser.getEmail())).thenReturn(testUser);

        orderRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void createOrder_shouldPersistOrderAndItems() {
        OrderDTO orderDTO = TestDataFactory.getTestOrderDTO(testUser.getId(), testUser.getEmail());

        OrderDTO createdOrder = orderService.createOrder(orderDTO);

        assertAll(() -> {
            assertNotNull(createdOrder.getId());
            assertEquals(1, orderRepository.count());
            assertEquals(1, itemRepository.count());
            assertEquals(2, createdOrder.getOrderItems().get(0).getQuantity());
            assertEquals("Laptop", createdOrder.getOrderItems().get(0).getItem().getName());
        });
    }

    @Test
    void getOrderById_shouldReturnOrderWithUserEmail() {
        OrderDTO created = orderService.createOrder(TestDataFactory.getTestOrderDTO(testUser.getId(), testUser.getEmail()));

        OrderResponseDTO response = orderService.getOrderById(created.getId());

        assertAll(() -> {
            assertEquals(created.getId(), response.getOrder().getId());
            assertEquals(testUser.getEmail(), response.getOrder().getUserEmail());
        });
    }

    @Test
    void updateOrder_shouldModifyOrderItems() {
        OrderDTO created = orderService.createOrder(TestDataFactory.getTestOrderDTO(testUser.getId(), testUser.getEmail()));

        OrderItemDTO updatedItem = new OrderItemDTO(
                null,
                new ItemDTO(null, "Updated Laptop", new BigDecimal("1200.00")),
                3
        );

        created.setOrderItems(List.of(updatedItem));

        OrderResponseDTO updated = orderService.updateOrder(created.getId(), created);

        assertAll(() -> {
            assertEquals(1, orderRepository.count());
            assertEquals("Updated Laptop", updated.getOrder().getOrderItems().get(0).getItem().getName());
            assertEquals(3, updated.getOrder().getOrderItems().get(0).getQuantity());
        });
    }

    @Test
    void deleteOrder_shouldRemoveOrderAndItems() {
        OrderDTO created = orderService.createOrder(TestDataFactory.getTestOrderDTO(testUser.getId(), testUser.getEmail()));

        orderService.deleteOrder(created.getId());

        assertAll(() -> {
            assertEquals(0, orderRepository.count());
            assertEquals(0, orderRepository.findAll().stream()
                    .flatMap(o -> o.getOrderItems().stream())
                    .count());
        });
    }

    @Test
    void getOrdersByStatuses_shouldReturnMatchingOrders() {
        OrderDTO order1 = TestDataFactory.getTestOrderDTO(testUser.getId(), testUser.getEmail());
        order1.setStatus("CREATED");
        orderService.createOrder(order1);

        OrderDTO order2 = TestDataFactory.getTestOrderDTO(testUser.getId(), testUser.getEmail());
        order2.setStatus("COMPLETED");
        orderService.createOrder(order2);

        List<OrderResponseDTO> results = orderService.getOrdersByStatuses(List.of("CREATED"));
        assertAll(() -> {
            assertEquals(1, results.size());
            assertEquals("CREATED", results.get(0).getOrder().getStatus());
        });
    }

    @Test
    void getOrdersByIds_shouldReturnMatchingOrders() {
        OrderDTO order1 = orderService.createOrder(TestDataFactory.getTestOrderDTO(testUser.getId(), testUser.getEmail()));
        OrderDTO order2 = orderService.createOrder(TestDataFactory.getTestOrderDTO(testUser.getId(), testUser.getEmail()));

        List<OrderResponseDTO> results = orderService.getOrdersByIds(List.of(order1.getId(), order2.getId()));

        assertAll(() -> {
            assertEquals(2, results.size());
            assertTrue(results.stream().anyMatch(o -> o.getOrder().getId().equals(order1.getId())));
            assertTrue(results.stream().anyMatch(o -> o.getOrder().getId().equals(order2.getId())));
        });
    }
}
