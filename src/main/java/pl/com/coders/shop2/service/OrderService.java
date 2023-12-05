package pl.com.coders.shop2.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.Order;
import pl.com.coders.shop2.domain.OrderLineItem;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.OrderDto;
import pl.com.coders.shop2.mapper.OrderMapper;
import pl.com.coders.shop2.repository.OrderLineItemRepository;
import pl.com.coders.shop2.repository.OrderRepository;
import pl.com.coders.shop2.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> findOrdersByUserId(Long userId) {
        List<Order> ordersByUserId = orderRepository.findOrdersByUserId(userId);
        return orderMapper.ordersToDto(ordersByUserId);
    }

    public List<OrderDto> findOrderById(UUID orderId) {
        List<Order> ordersById = orderRepository.findOrdersById(orderId);
        return orderMapper.ordersToDto(ordersById);
    }

    public OrderDto saveOrder(OrderDto orderDto) {
        Order order = orderMapper.dtoToOrder(orderDto);
        Order savedOrder = orderRepository.saveOrder(order);
        return orderMapper.orderToDto(savedOrder);
    }

    public boolean delete(UUID orderId) {
        return orderRepository.delete(orderId);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAllOrders();
    }
}

