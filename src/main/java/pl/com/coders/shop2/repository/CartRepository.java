package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;
import pl.com.coders.shop2.exceptions.EmptyCartException;
import pl.com.coders.shop2.exceptions.ProductNotFoundException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Repository
public class CartRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public static final String CART_ID = "cartId";
    private UserRepository userRepository;


    public CartRepository(EntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }

    public CartLineItem addProductToCart(Long cartId, String productTitle, BigDecimal amount) throws EmptyCartException, ProductNotFoundException {
        Cart cart = entityManager.find(Cart.class, cartId);
        if (cart == null) {
            cart = new Cart();
            entityManager.persist(cart);
        }

        Product product = entityManager.createQuery(
                        "SELECT p FROM Product p WHERE p.name = :productTitle", Product.class)
                .setParameter("productTitle", productTitle)
                .getResultList()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productTitle));

        CartLineItem cartLineItem = new CartLineItem();
        cartLineItem.setCart(cart);
        cartLineItem.setProduct(product);
        cartLineItem.setCartLineQuantity(cartLineItem.getCartLineQuantity() + amount.intValue());
        cartLineItem.setCartLinePrice(product.getPrice().multiply(amount));

        cart.getCartLineItems().add(cartLineItem);

        entityManager.persist(cartLineItem);

        return cartLineItem;
    }

    public Cart createCart(Cart newCart) {
        entityManager.merge(newCart);
        return newCart;
    }


    public Cart getCartByCartId(Long cartId) {
        String jpql = "SELECT c FROM Cart c WHERE c.id = :cartId";
        TypedQuery<Cart> query = entityManager.createQuery(jpql, Cart.class);
        query.setParameter(CART_ID, cartId);
        return query.getSingleResult();
    }


    public Cart getCartByUserId(Long userId) {
        String jpql = "SELECT c FROM Cart c WHERE c.user.id = :userId";
        TypedQuery<Cart> query = entityManager.createQuery(jpql, Cart.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    public Optional<Cart> getCartByUserEmail(String email) {
        String jpql = "SELECT c FROM Cart c WHERE c.user.email = :email";
        TypedQuery<Cart> query = entityManager.createQuery(jpql, Cart.class);
        query.setParameter("email", email);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex) {
            Cart newCart = new Cart();
            User user = userRepository.findByEmail(email);
            newCart.setUser(user);
            entityManager.persist(newCart);
            return Optional.of(newCart);
        }
    }

    public boolean deleteCartByCartId(Long cartId) {
        Query query = entityManager.createQuery("DELETE FROM Cart c WHERE c.id = :cartId");
        query.setParameter(CART_ID, cartId);
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

    public boolean deleteByCartIndex(Long cartId, int cartIndex) {
        Query query = entityManager.createQuery("DELETE FROM Cart c WHERE c.id = :cartId AND c.cartIndex = :cartIndex");
        query.setParameter(CART_ID, cartId);
        query.setParameter("cartIndex", cartIndex);
        int deletedCount = query.executeUpdate();
        return deletedCount > 0;
    }


    public int findCartLineItemIndex(Cart cart, CartLineItem cartLineItem) {
        Set<CartLineItem> cartLineItems = cart.getCartLineItems();
        Long cartItemId = cartLineItem.getId();

        Iterator<CartLineItem> iterator = cartLineItems.iterator();
        int index = 0;

        while (iterator.hasNext()) {
            CartLineItem existingCartItem = iterator.next();
            if (cartItemId.equals(existingCartItem.getId())) {
                return index;
            }
            index++;
        }

        return -1;
    }


}


