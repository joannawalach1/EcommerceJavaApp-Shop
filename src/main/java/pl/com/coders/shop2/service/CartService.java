package pl.com.coders.shop2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;
import pl.com.coders.shop2.exceptions.ProductNotFoundException;
import pl.com.coders.shop2.mapper.CartMapper;
import pl.com.coders.shop2.repository.CartRepository;
import pl.com.coders.shop2.repository.ProductRepository;
import pl.com.coders.shop2.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    
    public CartLineItemDto addProductToCart(String userEmail, String productTitle, int amount)
            throws ProductNotFoundException {
        CartLineItem cartLineItem = cartRepository.addProductToCart(userEmail, productTitle, amount);
        return cartMapper.cartLineItemToDto(cartLineItem);
    }

    public boolean deleteByCartIndex(int cartIndex) {
        cartRepository.deleteByCartIndex(cartIndex);
        return true;
    }

    public CartLineItem getCartByIndex(int cartIndex) {
        return cartRepository.findByCartIndex(cartIndex)
                .orElseThrow(() -> new NoSuchElementException("Cart not found with index: " + cartIndex));
    }
}
