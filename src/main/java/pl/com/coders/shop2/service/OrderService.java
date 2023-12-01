package pl.com.coders.shop2.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.Order;
import pl.com.coders.shop2.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public List<Order> findOrdersByUserId(Long userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

    public List<Order> findOrdersById(UUID orderId) {
        return orderRepository.findOrdersById(orderId);
    }

    public Order saveOrder(Order order) {
        return orderRepository.saveOrder(order);
    }

    public boolean delete(UUID orderId) {
        return orderRepository.delete(orderId);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAllOrders();
    }
}

