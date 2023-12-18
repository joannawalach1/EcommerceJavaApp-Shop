package pl.com.coders.shop2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.Order;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.OrderDto;
import pl.com.coders.shop2.mapper.OrderMapper;
import pl.com.coders.shop2.repository.OrderLineItemRepository;
import pl.com.coders.shop2.repository.OrderRepository;
import pl.com.coders.shop2.repository.ProductRepository;
import pl.com.coders.shop2.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private ProductRepository productRepository;


    public List<OrderDto> findOrdersByUserId(Long userId) {
        List<Order> ordersByUserId = orderRepository.findOrdersByUserId(userId);
        return orderMapper.ordersToDtos(ordersByUserId);
    }

    public List<OrderDto> findOrderById(UUID orderId) {
        List<Order> ordersById = orderRepository.findOrdersById(orderId);
        return orderMapper.ordersToDtos(ordersById);
    }

    public OrderDto saveOrder(OrderDto orderDto) {
        Order order = orderMapper.dtoToOrder(orderDto);
        order.setId(UUID.randomUUID());
        String userLastName = orderDto.getUserLastName();
        User user = userRepository.findByLastName(userLastName);

        if (user != null) {
            order.setUser(user);
        } else {
            throw new RuntimeException("Nie można znaleźć użytkownika o nazwisku: " + userLastName);
        }

        order.setTotalAmount(orderDto.getTotalAmount());
        order.setCreated(LocalDateTime.now());
        order.setUpdated(LocalDateTime.now());
        Order savedOrder = orderRepository.saveOrder(order);

        return orderMapper.orderToDto(savedOrder);
    }

    public boolean delete(UUID orderId) {
        return orderRepository.delete(orderId);
    }

    public List<OrderDto> findAllOrders() {
        List<Order> allOrders = orderRepository.findAllOrders();
        return orderMapper.ordersToDtos(allOrders);
    }
}

