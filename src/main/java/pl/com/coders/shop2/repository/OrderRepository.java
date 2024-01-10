package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Order;
import pl.com.coders.shop2.domain.OrderLineItem;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Repository
public class OrderRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final UserRepository userRepository;

    public OrderRepository(EntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }
    @Transactional
    public void saveOrder(Order order) {
        entityManager.merge(order);
    }

    @Transactional
    public void saveOrderLineItems(OrderLineItem orderLineItem) {
            entityManager.persist(orderLineItem);
    }

    @Transactional
    public Order findOrdersByUserEmail(String userEmail) {
        String jpql = "SELECT o FROM Order o WHERE o.user.id = :userId";
        TypedQuery<Order> query = entityManager.createQuery(jpql, Order.class);
        query.setParameter("userEmail", userEmail);
        return query.getSingleResult();
    }

    public boolean delete(UUID orderId) {
        entityManager.createQuery("DELETE FROM OrderLineItem oli WHERE oli.order.id = :orderId")
                .setParameter("orderId", orderId)
                .executeUpdate();

        Query query = entityManager.createQuery("DELETE FROM Order o WHERE o.id = :orderId");
        query.setParameter("orderId", orderId);

        int deletedCount = query.executeUpdate();
        return deletedCount > 0;
    }

    public List<Order> findAllOrders() {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o", Order.class);
        return query.getResultList();
    }
}

