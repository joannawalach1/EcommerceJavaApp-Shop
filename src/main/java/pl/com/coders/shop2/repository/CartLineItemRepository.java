package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.CartLineItem;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository
public class CartLineItemRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public CartLineItemRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public CartLineItem createCartLineItem(CartLineItem cartLineItem) {
        entityManager.persist(cartLineItem);
        return cartLineItem;
    }

    public void addCartLineItem(int cartIndex, CartLineItem newCartLineItem) {
        CartLineItem existingCart = entityManager.find(CartLineItem.class, cartIndex);
        if (existingCart == null) {
            existingCart = new CartLineItem();
            existingCart.setCart(newCartLineItem.getCart());
            existingCart.setProduct(newCartLineItem.getProduct());
            existingCart.setCartLinePrice(newCartLineItem.getCartLinePrice());
            existingCart.setCartLineQuantity(newCartLineItem.getCartLineQuantity());
            existingCart.setCartIndex(newCartLineItem.getCartIndex());
        } else {
            createCartLineItem(newCartLineItem);
        }
        int newCartIndex = cartIndex + 1;
        entityManager.persist(existingCart);
        existingCart.setCartIndex(newCartIndex);
    }

}


