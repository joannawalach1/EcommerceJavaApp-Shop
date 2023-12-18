package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class CartRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartRepository(EntityManager entityManager, UserRepository userRepository, ProductRepository productRepository, ProductRepository productRepository1) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
        this.productRepository = productRepository1;
    }

    public Cart createCart(String userEmail, String productTitle) {
        User user = userRepository.findByEmail(userEmail);
        Optional<Product> product = productRepository.getProductByName(productTitle);
        if (user == null) {
            throw new IllegalArgumentException("User not found for email: " + userEmail);
        }
        Cart cart = user.getCart();
        if (cart != null) {
            throw new IllegalArgumentException("User cart exists: " + cart);
        } else {
            cart = new Cart();
            cart.setUser(user);
            cart.setTotalPrice(product.get().getPrice());
            cart.setCreated(LocalDateTime.now());
            cart.setUpdated(LocalDateTime.now());
        }
        entityManager.merge(cart);
        return cart;
    }

    public Cart getCartByCartId(Long cartId) {
        String jpql = "SELECT c FROM Cart c WHERE c.id = :cartId";
        TypedQuery<Cart> query = entityManager.createQuery(jpql, Cart.class);
        query.setParameter("cartId", cartId);
        return query.getSingleResult();
    }


    public Cart getCartByUserId(Long userId) {
        String jpql = "SELECT c FROM Cart c WHERE c.user.id = :userId";
        TypedQuery<Cart> query = entityManager.createQuery(jpql, Cart.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    public Cart getCartByUserEmail(String email) {
        String jpql = "SELECT c FROM Cart c WHERE c.user.email = :email";
        TypedQuery<Cart> query = entityManager.createQuery(jpql, Cart.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }

    public boolean deleteCartByCartId(Long cartId) {
        Query query = entityManager.createQuery("DELETE FROM Cart c WHERE c.id = :cartId");
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

    public boolean deleteByCartIndex(Long cartId, int cartIndex) {
        Query query = entityManager.createQuery("DELETE FROM Cart c WHERE c.id = :cartId AND c.cartIndex = :cartIndex");
        query.setParameter("cartId", cartId);
        query.setParameter("cartIndex", cartIndex);
        int deletedCount = query.executeUpdate();
        return deletedCount > 0;
    }

}

