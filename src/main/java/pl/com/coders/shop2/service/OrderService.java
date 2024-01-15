package pl.com.coders.shop2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.*;
import pl.com.coders.shop2.domain.dto.OrderDto;
import pl.com.coders.shop2.mapper.OrderMapper;
import pl.com.coders.shop2.repository.CartRepository;
import pl.com.coders.shop2.repository.OrderRepository;
import pl.com.coders.shop2.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final CartRepository cartRepository;

    @Transactional
    public OrderDto createOrderFromCart(String userEmail, Long cartId) {
        Cart userCart = cartRepository.getCartForUser(userEmail);
        Order order = orderRepository.createOrder(userCart);
        for (CartLineItem cartLineItem : userCart.getCartLineItems()) {
            Product product = cartLineItem.getProduct();
            int quantity = cartLineItem.getCartLineQuantity();
            OrderLineItem orderLineItem = orderRepository.createOrderLineItem(order, product, quantity);
            order.addOrderLineItems(orderLineItem);

        }
        userCart.getCartLineItems().clear();
        cartRepository.updateCart(userCart);
        cartRepository.deleteCartAndItems(userCart.getId());
        return orderMapper.orderToDto(order);
    }


    public boolean delete(UUID orderId) {
        return orderRepository.delete(orderId);
    }

    public List<OrderDto> findAllOrders() {
        List<Order> allOrders = orderRepository.findAllOrders();
        return orderMapper.ordersToDto(allOrders);
    }
}

