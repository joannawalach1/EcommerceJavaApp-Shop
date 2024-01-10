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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final CartRepository cartRepository;

    @Transactional
    public OrderDto saveOrder(String userEmail, long cartId) {
        User user = userRepository.findByEmail(userEmail);
        Cart cart = cartRepository.getCartByCartId(cartId);
        if (user != null) {
            Order order = new Order();
            order.setId(UUID.randomUUID());
            order.setUser(user);
            //order.setTotalAmount(cart.getTotalPrice());
            order.setStatus("nowy");
            order.setOrderLineItems(new HashSet<>());
            order.setCreated(LocalDateTime.now());
            order.setUpdated(LocalDateTime.now());

            List<CartLineItem> cartsItems = cartRepository.getCartLineItemsByCartId(cartId);
            Set<OrderLineItem> orderLineItems = new HashSet<>();

            for (CartLineItem cartLineItem : cartsItems) {
                OrderLineItem orderLineItem = new OrderLineItem();
                Product product = cartLineItem.getProduct();
                orderLineItem.setOrder(order);
                order.setId(order.getId());
                orderLineItem.setProduct(product);
                orderLineItem.setQuantity(cartLineItem.getCartLineQuantity());
                orderLineItem.setPrice(cartLineItem.getCartLinePrice());
                order.addOrderLineItems(orderLineItem);
            }
            System.out.println("Przed zapisem order, id: {}" + order.getId());
            orderRepository.saveOrder(order);
            System.out.println("Po zapisie order, id: {}" + order.getId());
            cartRepository.deleteCart(cartId);

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

