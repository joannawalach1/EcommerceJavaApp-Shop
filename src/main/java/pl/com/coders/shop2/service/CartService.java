package pl.com.coders.shop2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.*;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;
import pl.com.coders.shop2.mapper.CartLineItemMapper;
import pl.com.coders.shop2.mapper.CartMapper;
import pl.com.coders.shop2.repository.CartLineItemRepository;
import pl.com.coders.shop2.repository.CartRepository;
import pl.com.coders.shop2.repository.ProductRepository;
import pl.com.coders.shop2.repository.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private CartMapper cartMapper;
    private CartLineItemMapper cartLineItemMapper;
    private CartLineItemRepository cartLineRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository, CartMapper cartMapper, CartLineItemMapper cartLineItemMapper, CartLineItemRepository cartLineRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
        this.cartLineItemMapper = cartLineItemMapper;
        this.cartLineRepository = cartLineRepository;
    }

    public CartDto saveCart(String productTitle, String userEmail) {
        User userOptional = userRepository.findByEmail(userEmail);
        Optional<Product> productOptional = productRepository.getProductByName(productTitle);
        Cart cart = userOptional.getCart();
        Cart cart1 = cartRepository.createCart(userOptional.getEmail(), productOptional.get().getName());
        return cartMapper.cartToDto(cart1);
    }

    public CartLineItemDto saveCartItem(String userEmail, String productTitle, BigDecimal amount) {
        Optional<CartLineItem> cartItem = cartLineRepository.createCartLineItem(userEmail,productTitle, amount);
        return cartLineItemMapper.cartLineItemToDto(cartItem);

    }

    public CartDto getCartByCartId(Long cartId) {
        Cart cartByCartId = cartRepository.getCartByCartId(cartId);
        return cartMapper.cartToDto(cartByCartId);
    }

    public CartDto getCartByUserId(Long userId) {
        Cart cartByUserId = cartRepository.getCartByUserId(userId);
        return cartMapper.cartToDto(cartByUserId);
    }

    public CartDto getCartByUserEmail(String email) {
        Cart cartByUserEmail = cartRepository.getCartByUserEmail(email);
        return cartMapper.cartToDto(cartByUserEmail);
    }

    public boolean delete(Long cartId) {
        return cartRepository.deleteCartByCartId(cartId);
    }

    public boolean deleteByCartIndex(Long cartId, int cartIndex) {
        return cartRepository.deleteByCartIndex(cartId, cartIndex);
    }

    public void deleteAll() {
        cartRepository.deleteAll();
    }

    public CartDto updateCartByCartId(CartDto cartDto) {
        Cart cart = cartMapper.dtoToCart(cartDto);
        Cart updatedCart = cartRepository.updateCartByCartId(cart);
        return cartMapper.cartToDto(updatedCart);
    }

    public List<CartDto> getAll() {
        List<CartDto> list = new ArrayList<>();
        CartMapper cartMapper1 = cartMapper;
        for (Cart cart1 : cartRepository.getAll()) {
            CartDto cartToDto = cartMapper1.cartToDto(cart1);
            list.add(cartToDto);
        }
        return list;
    }

}
