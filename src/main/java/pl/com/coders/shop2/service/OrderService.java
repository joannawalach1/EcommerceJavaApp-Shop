package pl.com.coders.shop2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Order;
import pl.com.coders.shop2.domain.OrderLineItem;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;
import pl.com.coders.shop2.domain.dto.OrderDto;
import pl.com.coders.shop2.exceptions.UserNotFoundException;
import pl.com.coders.shop2.mapper.CartMapper;
import pl.com.coders.shop2.mapper.OrderMapper;
import pl.com.coders.shop2.repository.CartRepository;
import pl.com.coders.shop2.repository.OrderRepository;
import pl.com.coders.shop2.repository.ProductRepository;
import pl.com.coders.shop2.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @Transactional
    public OrderDto createOrderFromCart(String userEmail, Long cartId) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedUser = userRepository.findByEmail(user);
        if (!userEmail.equals(loggedUser.getEmail())) {
            throw new UserNotFoundException("Podany email nie jest zalogowanym użytkownikiem.");
        }
        CartDto cartForAuthUser = cartService.getCartForAuthUser(loggedUser);
        Order order = orderRepository.createOrder(cartMapper.toCart(cartForAuthUser));
        order.setUser(loggedUser);
        for (CartLineItemDto cartLineItemDto : cartForAuthUser.getCartLineItems()) {
            Optional<Product> productToCart = productRepository.getProductByName(cartLineItemDto.getProductTitle());
            if (productToCart.isPresent()) {
                int quantity = cartLineItemDto.getCartLineQuantity();
                OrderLineItem orderLineItem = orderRepository.createOrderLineItem(order, productToCart, quantity);
                order.addOrderLineItems(orderLineItem);
            }
        }
        cartRepository.deleteCartAndItems(loggedUser.getId());
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

