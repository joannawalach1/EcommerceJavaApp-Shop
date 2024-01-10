package pl.com.coders.shop2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.exceptions.ProductNotFoundException;
import pl.com.coders.shop2.mapper.CartMapper;
import pl.com.coders.shop2.repository.CartRepository;
import pl.com.coders.shop2.repository.ProductRepository;
import pl.com.coders.shop2.repository.UserRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartDto addProductToCart(String userEmail, String productTitle, int amount)
            throws ProductNotFoundException {
        User user = userRepository.findByEmail(userEmail);
        Product product = getProduct(productTitle);
        Cart userCart = getOrCreateUserCart(userEmail, user);

        CartLineItem existingCartItem = findCartItem(userCart, product);
        if (existingCartItem == null) {
            existingCartItem = cartRepository.createCartLineItem(amount, userCart, product);
        } else {
            if (product.getQuantity() >= amount) {
                existingCartItem = cartRepository.updateCartLineItem(amount, existingCartItem, product);
            }
        }
        userCart.setTotalPrice(calculateCartTotalPrice(userCart));
        product.setQuantity(product.getQuantity() - amount);
        productRepository.update(product, product.getId());
        cartRepository.updateCart(userCart);
        return cartMapper.toDto(userCart);
    }


    private Cart getOrCreateUserCart(String userEmail, User user) {
        Cart userCart = cartRepository.getCartForUser(userEmail);
        if (userCart == null) {
            userCart = cartRepository.createCart(user);
        }
        return userCart;
    }

    private Product getProduct(String productTitle) throws ProductNotFoundException {
        return productRepository.getProductByName(productTitle)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productTitle));
    }

    private CartLineItem findCartItem(Cart cart, Product product) {
        return cart.getCartLineItems().stream()
                .filter(cli -> cli.getProduct().equals(product))
                .findFirst()
                .orElse(null);
    }

    public void deleteByIndex(long cartId, int cartIndex) {
        Cart cart = cartRepository.getCartByCartId(cartId);

        Optional<CartLineItem> cartLineItemOptional = cart.getCartLineItems().stream()
                .filter(item -> item.getCartIndex() == cartIndex)
                .findFirst();

        cartLineItemOptional.ifPresent(cartLineItem -> {
            cart.getCartLineItems().remove(cartLineItem);
            cartLineItem.getProduct().setQuantity(cartLineItem.getProduct().getQuantity());
            cart.setTotalPrice(calculateCartTotalPrice(cart));
            updateCartIndex(cart, cartIndex);
            cartRepository.updateCart(cart);
        });
    }

    private void updateCartIndex(Cart cart, int deletedIndex) {
        cart.getCartLineItems().stream()
                .filter(item -> item.getCartIndex() > deletedIndex)
                .toList()
                .forEach(cartItem ->  {
                    cartItem.setCartIndex(cartItem.getCartIndex()-1);
                });
    }


    private BigDecimal calculateCartTotalPrice(Cart userCart) {
        return userCart.getCartLineItems().stream()
                .map(CartLineItem::getCartLinePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

