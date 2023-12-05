package pl.com.coders.shop2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.mapper.CartMapper;
import pl.com.coders.shop2.repository.CartRepository;
import pl.com.coders.shop2.repository.ProductRepository;
import pl.com.coders.shop2.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private CartMapper cartMapper;
    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
    }


    private Cart cart;
    private CartDto cartDto;

    public CartDto saveCart(String productTitle, int cartLineQuantity, BigDecimal cartLinePrize, String userEmail) {
        Optional<Product> productByNameOptional = productRepository.getProductByName(productTitle);
        if (productByNameOptional.isEmpty()) {
            return null;
        }

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return null;
        }

        Cart cart = cartRepository.getCartByUserId(user.getId());
        Product product = productByNameOptional.orElse(null);
        Optional<CartLineItem> existingCartItem = cart.getCartLineItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();
        if (existingCartItem.isPresent()) {
            existingCartItem.get().setCartLineQuantity(existingCartItem.get().getCartLineQuantity() + cartLineQuantity);
            existingCartItem.get().setCartLinePrice(cartLinePrize);
        } else {
            CartLineItem cartLineItem = CartLineItem.builder()
                    .cart(cart)
                    .product(productByNameOptional.get())
                    .cartLineQuantity(cartLineQuantity)
                    .cartLinePrice(cartLinePrize)
                    .cartIndex(cart.getCartLineItems().size() + 1)
                    .build();
            cartRepository.addToCartLineItem(cartLineItem);
        }
        return cartMapper.cartToDto(cartRepository.saveCart(cart));
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

    public void deleteAll() {
        cartRepository.deleteAll();
    }

    public CartDto updateCartByCartId(CartDto cartDto) {
        cart = cartMapper.dtoToCart(cartDto);
        Cart updatedCart = cartRepository.updateCartByCartId(cart);
        return cartMapper.cartToDto(updatedCart);
    }

    public List<CartDto> getAll() {
        return cartRepository.getAll().stream()
                .map(cartMapper::cartToDto)
                .collect(Collectors.toList());
    }

}
