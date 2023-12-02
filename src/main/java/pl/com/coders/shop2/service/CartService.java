package pl.com.coders.shop2.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.mapper.CartMapper;
import pl.com.coders.shop2.repository.CartRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private CartMapper cartMapper;
    private Cart cart;


    public CartDto saveCart(CartDto cartDto) {
        Cart cart = cartMapper.dtoToCart(cartDto);
        cart = cartRepository.saveCart(cart);
        return cartMapper.cartToDto(cart);
    }
    public CartDto getCartByCartId(Long cartId) {
        Cart cartByCartId = cartRepository.getCartByCartId(cartId);
        return cartMapper.cartToDto(cartByCartId);
    }

    public CartDto getCartByUserId(Long userId) {
        Cart cartByUserId = cartRepository.getCartByUserId(userId);
        return cartMapper.cartToDto(cartByUserId);
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
        List<Cart> carts = cartRepository.getAll();
        return cartMapper.cartToDto(carts);
    }
}
