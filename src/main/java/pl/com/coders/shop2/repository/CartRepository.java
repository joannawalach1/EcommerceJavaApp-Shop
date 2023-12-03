package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.CartLineItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class CartRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public CartRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Cart saveCart(Cart cart) {
        entityManager.persist(cart);
        return cart;
    }

    public Cart getCartByCartId(Long cartId) {
        String jpql = "SELECT c FROM Cart c WHERE c.id = :cartId";
        TypedQuery<Cart> query = entityManager.createQuery(jpql, Cart.class);
        query.setParameter("cartId", cartId);
        return query.getSingleResult();
    }

    public Cart getCartByUserId(Long userId) {
        String jpql = "SELECT c FROM Cart c WHERE c.id = :userId";
        TypedQuery<Cart> query = entityManager.createQuery(jpql, Cart.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    public boolean deleteCartByCartId(Long cartId) {
        Query query = entityManager.createQuery("DELETE FROM Order p WHERE p.id = :orderId");
        query.setParameter("cartId", cartId);
        return true;
    }

    public void deleteAll() {
        String jpql = "DELETE FROM Cart";
        entityManager.createQuery(jpql).executeUpdate();
        entityManager.flush();
    }

    public Cart updateCartByCartId(Cart cart) {
        cart.setUpdated(LocalDateTime.now());
        entityManager.merge(cart);
        return getCartByCartId(cart.getId());
    }

    public List<Cart> getAll() {
        TypedQuery<Cart> query = entityManager.createQuery("SELECT c FROM Cart c", Cart.class);
        return query.getResultList();
    }

    public Integer getSize() {
        String jpql = "SELECT COUNT(c) FROM Cart c";
        Query query = entityManager.createQuery(jpql);
        return Math.toIntExact((Long) query.getSingleResult());
    }

    public void addToCartLineItem(CartLineItem cartLineItem) {
        Cart cart = entityManager.find(Cart.class, cartLineItem.getCart().getId());
        if (cart != null) {
            if (cart.getCartLineItems() == null) {
                cart.setCartLineItems(new ArrayList<>());
            }

            cart.getCartLineItems().add(cartLineItem);
            cartLineItem.setCart(cart);

            entityManager.merge(cart);
        }
    }

    public Cart getCartByUserEmail(String email) {
        String jpql = "SELECT c FROM Cart c WHERE c.email = :email";
        TypedQuery<Cart> query = entityManager.createQuery(jpql, Cart.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }
}
