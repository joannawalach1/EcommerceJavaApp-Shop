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

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final CartRepository cartRepository;

    @Transactional
    public OrderDto saveOrder(String userEmail, Long cartId) {
        User user = userRepository.findByEmail(userEmail);
        Cart cart = cartRepository.getCartByCartId(cartId);
        if (user != null) {
            Order order = new Order();
            order.setId(UUID.randomUUID());
            order.setUser(user);
            order.setTotalAmount(cart.getTotalPrice());
            order.setStatus("nowy");
            order.setOrderLineItems(new HashSet<>());
            order.setCreated(LocalDateTime.now());
            order.setUpdated(LocalDateTime.now());

            List<CartLineItem> cartsItems = cartRepository.getCartLineItemsByCartId(cartId);
            Set<OrderLineItem> orderLineItems = new HashSet<>();

            for (CartLineItem cartLineItem : cartsItems) {
                OrderLineItem orderLineItem = new OrderLineItem();
                orderLineItem.setProduct(cartLineItem.getProduct());
                orderLineItem.setPrice(cartLineItem.getCartLinePrice());
                orderLineItem.setQuantity(cartLineItem.getCartLineQuantity());
                order.addOrderLineItems(orderLineItem);

                order.addOrderLineItems(orderLineItem);
            }
            orderRepository.createOrder(order);
           // cartRepository.deleteCartAndItems(cartId, cart);

            return orderMapper.orderToDto(order);
        } else {
            throw new RuntimeException("Nie można znaleźć użytkownika o nazwisku: ");
        }
    }

    public boolean delete(UUID orderId) {
        return orderRepository.delete(orderId);
    }

    public List<OrderDto> findAllOrders() {
        List<Order> allOrders = orderRepository.findAllOrders();
        return orderMapper.ordersToDtos(allOrders);
    }
}

