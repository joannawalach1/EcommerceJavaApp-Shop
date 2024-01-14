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
            cartRepository.createCartLineItem(amount, userCart, product);
        } else {
            if (product.getQuantity() >= amount) {
                cartRepository.updateCartLineItem(amount, existingCartItem, product);
            }
            updateAfterAddingItem(userCart, existingCartItem, amount);
            productRepository.update(product, product.getId());
        }
        productRepository.update(product, product.getId());
        cartRepository.updateCart(userCart);
        return cartMapper.toDto(userCart);
    }


    public Cart getOrCreateUserCart(String userEmail, User user) {
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
                .filter(cli -> cli.getProduct().getName().equals(product.getName()))
                .findFirst()
                .orElse(null);
    }

    public void deleteByIndex(long cartId, int cartIndex) {
        Cart cart = cartRepository.getCartByCartId(cartId);

        if (cart != null) {
            Optional<CartLineItem> cartLineItemOptional = cart.getCartLineItems().stream()
                    .filter(item -> item.getCartIndex() == cartIndex)
                    .findFirst();

            cartLineItemOptional.ifPresent(cartLineItem -> {
                cart.getCartLineItems().remove(cartLineItem);
                updateCartAfterItemRemoval(cart, cartLineItem);
                cart.setTotalPrice(cartRepository.calculateCartTotalPrice(cart));
                cartRepository.updateCart(cart);
            });

            if (cart.getCartLineItems().isEmpty() && cart.getTotalPrice().compareTo(BigDecimal.ZERO) == 0) {
                cartRepository.deleteCart(cartId);
            }
        }
    }

    private void updateCartAfterItemRemoval(Cart cart, CartLineItem removedItem) {
        Product product = removedItem.getProduct();
        product.setQuantity(product.getQuantity() + removedItem.getCartLineQuantity());

        cart.setTotalPrice(cartRepository.calculateCartTotalPrice(cart));
        updateCartIndex(cart, removedItem.getCartIndex());
    }

    private void updateAfterAddingItem(Cart cart, CartLineItem addedItem, int amount) {
        if (addedItem != null) {
            Product product = addedItem.getProduct();
            int remainingQuantity = Math.max(product.getQuantity() - amount, 0);
            product.setQuantity(remainingQuantity);
        }
    }


    private void updateCartIndex(Cart cart, int deletedIndex) {
        cart.getCartLineItems().stream()
                .filter(item -> item.getCartIndex() > deletedIndex)
                .forEach(cartItem -> cartItem.setCartIndex(cartItem.getCartIndex() - 1));
    }
}


