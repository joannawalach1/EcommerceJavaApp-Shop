package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.Order;
import pl.com.coders.shop2.domain.OrderLineItem;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Transactional
@Repository
public class OrderLineItemRepository {
    @PersistenceContext
    private EntityManager entityManager;


    public boolean deleteByProductId(Long productId) {
        Query query = entityManager.createQuery("DELETE FROM OrderLineItem oli WHERE oli.product.id = :productId");
        query.setParameter("productId", productId);
        query.executeUpdate();
        return true;
    }

    public OrderLineItem saveOrderLineItem(OrderLineItem orderLineItem) {
        entityManager.persist(orderLineItem);
        return orderLineItem;
    }

}
