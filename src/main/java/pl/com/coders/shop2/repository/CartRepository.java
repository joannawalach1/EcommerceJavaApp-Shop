package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.exceptions.ProductNotFoundException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Transactional
@Repository
public class CartRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    private final UserRepository userRepository;

    public CartRepository(EntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }

    public CartLineItem addProductToCart(String userEmail, String productTitle, int amount) throws ProductNotFoundException {
        Cart userCart = getUserCart(userEmail);
        Product product = getProduct(productTitle);
        int availableQuantity = product.getQuantity();

        if (userCart == null) {
            User user = userRepository.findByEmail(userEmail);
            userCart = createNewCart(user);
            entityManager.flush();
        } else {
            updateCart(userCart);
        }

        if (amount > availableQuantity) {
            throw new IllegalArgumentException("Requested quantity exceeds available quantity");
        }
        CartLineItem existingCartItem = updateCartLineItem(amount, userCart, product);
        entityManager.flush();
        updateCart(userCart);
        return existingCartItem;
    }


    public boolean deleteByCartIndex(int cartIndex) {
       TypedQuery<CartLineItem> query = entityManager.createQuery("SELECT cli FROM CartLineItem cli WHERE cli.cartIndex = :cartIndex", CartLineItem.class);
        query.setParameter("cartIndex", cartIndex);

        try {
            CartLineItem cartLineItem = query.getSingleResult();
            Cart cart = cartLineItem.getCart();
            Product product = cartLineItem.getProduct();
            int removedQuantity = cartLineItem.getCartLineQuantity();
            updateProductQuantity(product, removedQuantity);
            cart.getCartLineItems().remove(cartLineItem);
            entityManager.remove(cartLineItem);
            cart.setTotalPrice(calculateTotalPriceForCart(cart.getCartLineItems()));
            entityManager.merge(cart);
            entityManager.flush();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    private CartLineItem updateCartLineItem(int amount, Cart userCart, Product product) {
        CartLineItem existingCartItem = findCartItem(userCart, product);
        if (existingCartItem == null) {
            existingCartItem = createCartLineItem(amount, userCart, product);
        } else {
            int newQuantity = existingCartItem.getCartLineQuantity() + amount;
            existingCartItem.setCartLineQuantity(newQuantity);

            BigDecimal newPrice = product.getPrice().multiply(BigDecimal.valueOf(newQuantity));
            existingCartItem.setCartLinePrice(newPrice);
            updateProductQuantity(product, amount);
            entityManager.merge(existingCartItem);
        }
        return existingCartItem;
    }

    private CartLineItem findCartItem(Cart cart, Product product) {
        Set<CartLineItem> cartLineItems = cart.getCartLineItems();
        for (CartLineItem cartLineItem : cartLineItems) {
            if (cartLineItem.getProduct().equals(product)) {
                return cartLineItem;
            }
        }
        return null;
    }

    private Product getProduct(String productTitle) throws ProductNotFoundException {
        return entityManager.createQuery(
                        "SELECT p FROM Product p WHERE p.name = :productTitle", Product.class)
                .setParameter("productTitle", productTitle)
                .getResultList()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productTitle));
    }

    private Cart getUserCart(String userEmail) {
        return getCartByUserEmail(userEmail)
                .orElseGet(() -> {
                    User user = userRepository.findByEmail(userEmail);
                    return createNewCart(user);
                });
    }

    private Cart createNewCart(User user) {
        Cart newCart = new Cart();
        newCart.setUser(user);
        newCart.setCartLineItems(newCart.getCartLineItems());
        newCart.setTotalPrice(calculateTotalPriceForCart(newCart.getCartLineItems()));
        entityManager.persist(newCart);
        return newCart;
    }

    private void updateCart(Cart userCart) {
        userCart.setTotalPrice(calculateTotalPriceForCart(userCart.getCartLineItems()));
        entityManager.merge(userCart);
        entityManager.flush();
    }

    private CartLineItem createCartLineItem(int amount, Cart cart, Product product) {
        CartLineItem cartLineItem = new CartLineItem();
        cartLineItem.setCart(cart);
        cartLineItem.setProduct(product);
        cartLineItem.setCartLineQuantity(cartLineItem.getCartLineQuantity() + amount);
        updateProductQuantity(product, amount);
        cartLineItem.setCartLinePrice(product.getPrice().multiply(BigDecimal.valueOf(amount)));
        cartLineItem.setCartIndex(generateCartIndex());
        entityManager.persist(cartLineItem);
        return cartLineItem;
    }

//    private void removeCartLineItem(CartLineItem existingCartItem) {
//        cart = existingCartItem.getCart();
//        int removedAmount = existingCartItem.getCartLineQuantity();
//        cart.getCartLineItems().remove(existingCartItem);
//        cart.setTotalPrice(calculateTotalPriceForCart(cart.getCartLineItems()));
//        updateProductQuantity(existingCartItem.getProduct(), removedAmount);
//        entityManager.remove(existingCartItem);
//        entityManager.merge(cart);
//        entityManager.flush();
//    }

    private static BigDecimal calculateTotalPriceForCart(Set<CartLineItem> cartItems) {
        return cartItems.stream()
                .map(CartLineItem::getCartLinePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void updateProductQuantity(Product product, int quantityToUpdate) {
        int currentQuantity = product.getQuantity();
        int newQuantity = currentQuantity - quantityToUpdate;
        product.setQuantity(newQuantity);
        entityManager.merge(product);
    }

    private static void restoreProductQuantity(Product product, int quantityToRestore) {
        int currentQuantity = product.getQuantity();
        int newQuantity = currentQuantity + quantityToRestore;
        product.setQuantity(newQuantity);
    }

    public Optional<Cart> getCartByUserEmail(String email) {
        String jpql = "SELECT c FROM Cart c WHERE c.user.email = :email";
        TypedQuery<Cart> query = entityManager.createQuery(jpql, Cart.class);
        query.setParameter("email", email);

        List<Cart> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));    }

    private int generateCartIndex() {
        Integer lastCartIndex = entityManager.createQuery("SELECT MAX(c.cartIndex) FROM CartLineItem c", Integer.class)
                .getSingleResult();

        return (lastCartIndex != null) ? (lastCartIndex + 1) : 1;
    }


    public Optional<CartLineItem> findByCartIndex(int cartIndex) {
        TypedQuery<CartLineItem> query = entityManager.createQuery("SELECT cli FROM CartLineItem cli WHERE cli.cartIndex = :cartIndex", CartLineItem.class);
        query.setParameter("cartIndex", cartIndex);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}


