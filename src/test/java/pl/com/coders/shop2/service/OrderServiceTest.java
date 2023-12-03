package pl.com.coders.shop2.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.coders.shop2.domain.Order;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.repository.OrderRepository;
import pl.com.coders.shop2.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    private List<Order> orders;

    @BeforeEach
    void setUp() {
        orders = prepareTestData();
    }

    @Test
    void findOrdersByUserId() {
        Long userId = 1L;
        when(orderRepository.findOrdersByUserId(any())).thenReturn(orders);
        List<Order> foundOrders = orderService.findOrdersByUserId(userId);
        assertNotNull(foundOrders);
        assertEquals(orders, foundOrders);
        verify(orderRepository, times(1)).findOrdersByUserId(userId);
    }

    @Test
    void findOrdersById() {
        UUID orderId = UUID.fromString("a0a1ab07-d158-4e89-8b42-2fd8c677147f");
        when(orderRepository.findOrdersById(orderId)).thenReturn(orders);
        List<Order> foundOrders = orderService.findOrdersById(orderId);
        assertNotNull(foundOrders);
        assertEquals(orders, foundOrders);
        verify(orderRepository, times(1)).findOrdersById(orderId);
    }

    @Test
    void delete() {
        UUID orderId = UUID.fromString("a0a1ab07-d158-4e89-8b42-2fd8c677147f");
        when(orderRepository.delete(orderId)).thenReturn(true);
        boolean result = orderService.delete(orderId);
        assertTrue(result, "Expected delete to return true");
        verify(orderRepository, times(1)).delete(orderId);
    }

    @Test
    void findAllOrders() {
        when(orderRepository.findAllOrders()).thenReturn(orders);
        List<Order> foundOrders = orderService.findAllOrders();
        assertNotNull(foundOrders);
        assertEquals(orders, foundOrders);
        verify(orderRepository, times(1)).findAllOrders();
    }

    private List<Order> prepareTestData() {
        User user1 = userRepository.create(new User(1L, "john@example.com", "John", "Doe", "pass1"));
        User user2 = userRepository.create(new User(2L, "anne@example.com", "Anne", "Boe", "pass2"));
        Order order1 = orderRepository.saveOrder(new Order(user1, 100.0));
        Order order2 = orderRepository.saveOrder(new Order(user2, 50));
        return Arrays.asList(order1, order2);
    }
}
