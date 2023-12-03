package pl.com.coders.shop2.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.coders.shop2.domain.Order;
import pl.com.coders.shop2.domain.User;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        prepareTestData();
    }

    @Test
    void findOrdersByUserId() {
        List<Order> ordersByUser = orderRepository.findOrdersByUserId(1L);
        assertNotNull(ordersByUser);
    }

    @Test
    void findOrdersById() {
        List<Order> ordersById = orderRepository.findOrdersById(UUID.fromString("a0a1ab07-d158-4e89-8b42-2fd8c677147f"));
        assertNotNull(ordersById);
    }

    @Test
    void delete() {
        boolean deletedOrders = orderRepository.delete(UUID.fromString("a0a1ab07-d158-4e89-8b42-2fd8c677147f"));
        assertTrue(deletedOrders);
    }

    @Test
    void findAllOrders() {
        List<Order> orders = orderRepository.findAllOrders();
        assertEquals(8, orders.size());
    }

    private List<Order> prepareTestData() {
        User user1 = userRepository.create(new User(1L, "john@example.com", "John", "Doe", "pass1"));
        User user2 = userRepository.create(new User(2L, "anne@example.com", "Anne", "Boe", "pass2"));
        Order order1 = orderRepository.saveOrder(new Order(user1, 100.0));
        Order order2 = orderRepository.saveOrder(new Order(user2, 50));
        return Arrays.asList(order1, order2);}
}