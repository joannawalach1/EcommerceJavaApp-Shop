//package pl.com.coders.shop2.repository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import pl.com.coders.shop2.domain.Order;
//
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class OrderRepositoryTest {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private List<Order> allOrders;
//
//    @BeforeEach
//    void setUp() {
//
//    }
//
//    @Test
//    void findOrdersByUserId() {
//        List<Order> ordersByUser = orderRepository.findOrdersByUserId(1L);
//        assertNotNull(ordersByUser);
//    }
//
//    @Test
//    void findOrdersById() {
//        List<Order> ordersById = orderRepository.findOrdersById(UUID.fromString("a0a1ab07-d158-4e89-8b42-2fd8c677147f"));
//        assertNotNull(ordersById);
//    }
//
//    @Test
//    void delete() {
//        boolean deletedOrders = orderRepository.delete(UUID.fromString("a0a1ab07-d158-4e89-8b42-2fd8c677147f"));
//        assertTrue(deletedOrders);
//    }
//
//    @Test
//    void findAllOrders() {
//        List<Order> orders = orderRepository.findAllOrders();
//        assertEquals(1, orders.size());
//    }
//}