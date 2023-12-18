package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Optional;

@Transactional
@Repository
public class CartLineItemRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private Cart cart;
    private CartRepository cartRepository;

    public CartLineItemRepository(EntityManager entityManager, ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository) {
        this.entityManager = entityManager;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public Optional<CartLineItem> createCartLineItem(String userEmail, String productTitle, BigDecimal amount) {
        User user = userRepository.findByEmail(userEmail);
        Optional<Product> product = productRepository.getProductByName(productTitle);
        product.get().setQuantity(product.get().getQuantity() - amount.intValue());

        Optional<CartLineItem> cartItem = user.getCart().getCartLineItems().stream()
                .filter(item -> item.getProduct().getName().equals(productTitle))
                .findFirst();

        if (cartItem.isPresent()) {
            CartLineItem existingCartItem = cartItem.get();
            existingCartItem.setCart(user.getCart());
            existingCartItem.setCartLineQuantity(existingCartItem.getCartLineQuantity()+(amount.intValue()));
            existingCartItem.setCartLinePrice(existingCartItem.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(existingCartItem.getCartLineQuantity())));
            return Optional.of(existingCartItem);
        } else {
            CartLineItem newCartItem = CartLineItem.builder()
                    .cart(user.getCart())
                    .product(product.get())
                    .cartLineQuantity(amount.intValue())
                    .cartLinePrice(product.get().getPrice().multiply(amount))
                    .cartIndex(user.getCart().getCartLineItems().size() + 1)
                    .build();
            user.getCart().addCartLineItem(newCartItem);
            entityManager.persist(newCartItem);
            return Optional.of(newCartItem);
        }
    }
}


