package pl.com.coders.shop2.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.exceptions.UserNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
@RequiredArgsConstructor
public class CartRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final UserRepository userRepository;

    public Cart createCart(User user) {
        Cart newCart = new Cart();
        newCart.setUser(user);
        newCart.setCartLineItems(new ArrayList<>());
        newCart.setTotalPrice(BigDecimal.ZERO);
        entityManager.persist(newCart);
        return newCart;
    }

    public Cart updateCart(Cart userCart) {
        entityManager.merge(userCart);
        return userCart;
    }

    public CartLineItem createCartLineItem(int amount, Cart cart, Product product) {
        CartLineItem cartLineItem = new CartLineItem();
        cartLineItem.setCart(cart);
        cartLineItem.setProduct(product);
        cartLineItem.setCartLineQuantity(amount);
        cartLineItem.setCartLinePrice(product.getPrice().multiply(BigDecimal.valueOf(amount)));
        cartLineItem.setCartIndex(generateCartIndex(cart));
        entityManager.persist(cartLineItem);
        return cartLineItem;
    }

    public CartLineItem updateCartLineItem(int amount, CartLineItem existingCartItem, Product product) {
        int newQuantity = existingCartItem.getCartLineQuantity() + amount;
        existingCartItem.setCartLineQuantity(newQuantity);
        existingCartItem.setCartLinePrice(product.getPrice().multiply(BigDecimal.valueOf(newQuantity)));
        entityManager.merge(existingCartItem);
        return existingCartItem;
    }

    public int generateCartIndex(Cart cart) {
        Integer lastCartIndex = cart.getCartLineItems().size();
        return lastCartIndex + 1;
    }

    public Cart getCartForUser(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User not found for email: " + email);
        }

        Cart existingCart = entityManager.createQuery("SELECT c FROM Cart c JOIN FETCH c.user WHERE c.user = :user", Cart.class)
                .setParameter("user", user)
                .getResultList().stream().findFirst().orElse(null);

        if (existingCart == null) {
            existingCart = createCart(user);
        }
        return existingCart;
    }

    public CartLineItem getCartLineItemByIndex(int cartIndex) {
        TypedQuery<CartLineItem> query = entityManager.createQuery(
                "SELECT cli FROM CartLineItem cli WHERE cli.cartIndex = :cartIndex", CartLineItem.class);
        query.setParameter("cartIndex", cartIndex);
        return query.getSingleResult();
    }

    @Transactional
    public List<CartLineItem> getCartLineItemsByCartId(long cartId) {
        return entityManager.createQuery("SELECT c FROM CartLineItem c WHERE c.cart.id = :cartId", CartLineItem.class)
                .setParameter("cartId", cartId)
                .getResultList();
    }

    @Transactional
        public void deleteCart(long cartId) {
            Cart cartToRemove = entityManager.find(Cart.class, cartId);
            if (cartToRemove == null) {
                entityManager.remove(cartToRemove);
            }
    }
    public Cart getCartByCartId(long cartId) {
        return entityManager.find(Cart.class, cartId);
    }
}


