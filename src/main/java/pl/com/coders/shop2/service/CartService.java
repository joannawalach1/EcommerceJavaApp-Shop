package pl.com.coders.shop2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;
import pl.com.coders.shop2.exceptions.ProductNotFoundException;
import pl.com.coders.shop2.mapper.CartMapper;
import pl.com.coders.shop2.repository.CartRepository;
import pl.com.coders.shop2.repository.ProductRepository;
import pl.com.coders.shop2.repository.UserRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartLineItemDto addProductToCart(String userEmail, String productTitle, int amount)
            throws ProductNotFoundException {
        User user = userRepository.findByEmail(userEmail);
        Cart userCart = getOrCreateUserCart(userEmail, user);
        Product product = getProduct(productTitle);

        CartLineItem existingCartItem = updateCartLineItem(amount, userCart, product);
        return cartMapper.cartLineItemToDto(existingCartItem);
    }

    public void deleteByIndex(int cartIndex) {
        CartLineItem cartLineItem = cartRepository.getCartLineItemByIndex(cartIndex);

        if (cartLineItem != null) {
            Cart cart = cartLineItem.getCart();
            cart.getCartLineItems().remove(cartLineItem);
            restoreProductQuantity(cartLineItem.getProduct(), cartLineItem.getCartLineQuantity());
            cart.setTotalPrice(calculateCartTotalPrice(cart));
            cartRepository.updateCart(cart);
        }
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

    private CartLineItem updateCartLineItem(int amount, Cart userCart, Product product) {
        CartLineItem existingCartItem = findCartItem(userCart, product);
        if (existingCartItem == null) {
            existingCartItem = cartRepository.createCartLineItem(amount, userCart, product);
        } else {
            int newQuantity = existingCartItem.getCartLineQuantity() + amount;
            if (newQuantity > 0) {
                cartRepository.updateCartLineItem(amount, existingCartItem, product);
            } else {
                userCart.getCartLineItems().remove(existingCartItem);
                //cartRepository.removeCart(userCart);
            }
        }
        updateProductQuantity(product, amount);
        existingCartItem.setCartLinePrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getCartLineQuantity())));
        userCart.setTotalPrice(product.getPrice()
                .multiply(BigDecimal.valueOf(existingCartItem.getCartLineQuantity())));
        cartRepository.updateCart(userCart);
        return existingCartItem;
    }


    private CartLineItem findCartItem(Cart cart, Product product) {
        return cart.getCartLineItems().stream()
                .filter(cli -> cli.getProduct().equals(product))
                .findFirst()
                .orElse(null);
    }



    private BigDecimal calculateCartTotalPrice(Cart userCart) {
        return userCart.getCartLineItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getCartLineQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void updateProductQuantity(Product product, int quantityToUpdate) {
        int currentQuantity = product.getQuantity();
        int newQuantity = currentQuantity - quantityToUpdate;
        product.setQuantity(newQuantity);
        productRepository.update(product, product.getId());
    }

    private void restoreProductQuantity(Product product, int quantityToRestore) {
        int currentQuantity = product.getQuantity();
        int newQuantity = currentQuantity + quantityToRestore;
        product.setQuantity(newQuantity);
        productRepository.update(product, product.getId());
    }

//    public void removeEmptyCarts() {
//        List<Cart> emptyCarts = cartRepository.getEmptyCarts();
//
//        for (Cart emptyCart : emptyCarts) {
//            if (emptyCart.getTotalPrice().compareTo(BigDecimal.ZERO) == 0) {
//                cartRepository.removeCart(emptyCart);
//            }
//        }
//    }
}

