package pl.com.coders.shop2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;
import pl.com.coders.shop2.exceptions.CartLineItemCreationException;
import pl.com.coders.shop2.exceptions.EmptyCartException;
import pl.com.coders.shop2.exceptions.ProductNotFoundException;
import pl.com.coders.shop2.helpers.CartTotalCalculator;
import pl.com.coders.shop2.mapper.CartMapper;
import pl.com.coders.shop2.repository.CartRepository;
import pl.com.coders.shop2.repository.ProductRepository;
import pl.com.coders.shop2.repository.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    private CartTotalCalculator cartTotalCalculator;

    public CartLineItemDto addOrUpdateCartItem(String userEmail, String productTitle, BigDecimal amount)
            throws ProductNotFoundException, CartLineItemCreationException, EmptyCartException {
        Cart userCart = cartRepository.getCartByUserEmail(userEmail)
                .orElseGet(() -> {
                    User user = userRepository.findByEmail(userEmail);
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.createCart(newCart);
                });

        Product product = productRepository.getProductByName(productTitle)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productTitle));

        CartLineItem cartItem = cartRepository.addProductToCart(userCart.getId(), product.getName(), amount);

        if (cartItem == null) {
            throw new CartLineItemCreationException("Failed to create cart line item for user: " + userEmail);
        }
        product.setQuantity(product.getQuantity() - cartItem.getCartLineQuantity());

        Set<CartLineItem> cartItems = userCart.getCartLineItems();
        BigDecimal totalPrice = cartTotalCalculator.calculateTotalPriceForCart(cartItems);
        userCart.setTotalPrice(totalPrice);
        cartRepository.createCart(userCart);
        CartLineItemDto cartItemDto = new CartLineItemDto();
        cartItemDto.setProductTitle(productTitle);
        cartItemDto.setCartLineQuantity(cartItem.getCartLineQuantity());
        cartItemDto.setCartLinePrice(cartItem.getCartLinePrice());
        cartItemDto.setCartIndex(cartRepository.findCartLineItemIndex(userCart, cartItem));

        return cartMapper.cartLineItemToDto(cartItemDto);
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
        Optional<Cart> cartByUserEmail = cartRepository.getCartByUserEmail(email);
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
