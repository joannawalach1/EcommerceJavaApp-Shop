package pl.com.coders.shop2.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.coders.shop2.domain.Order;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.OrderDto;
import pl.com.coders.shop2.mapper.OrderMapper;
import pl.com.coders.shop2.repository.OrderRepository;
import pl.com.coders.shop2.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testFindOrdersByUserId() {
        Long userId = 1L;
        when(orderRepository.findOrdersByUserId(userId)).thenReturn(List.of(new Order()));
        List<OrderDto> result = orderService.findOrdersByUserId(userId);
        assertNotNull(result);
    }

    @Test
    void testFindOrderById() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findOrdersById(orderId)).thenReturn(List.of(new Order()));
        List<OrderDto> result = orderService.findOrderById(orderId);
        assertNotNull(result);
    }

    @Test
    void testSaveOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setUserLastName("Doe");
        orderDto.setTotalAmount(BigDecimal.valueOf(100.0));

        User user = new User();
        user.setLastName("Doe");

        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setUser(user);
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setCreated(LocalDateTime.now());
        order.setUpdated(LocalDateTime.now());

        when(orderMapper.dtoToOrder(any())).thenReturn(order);
        when(userRepository.findByLastName(any())).thenReturn(user);
        when(orderRepository.saveOrder(any())).thenReturn(order);
        when(orderMapper.orderToDto(any())).thenReturn(orderDto);

        OrderDto result = orderService.saveOrder(orderDto);

        assertNotNull(result);
        assertEquals(order.getUser().getLastName(), result.getUserLastName());
        assertEquals(order.getTotalAmount(), result.getTotalAmount());
    }

    @Test
    void testDelete() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.delete(orderId)).thenReturn(true);
        assertTrue(orderService.delete(orderId));
    }

    @Test
    void testFindAllOrders() {
        when(orderRepository.findAllOrders()).thenReturn(List.of(new Order()));
        List<OrderDto> result = orderService.findAllOrders();
        assertNotNull(result);
    }
}