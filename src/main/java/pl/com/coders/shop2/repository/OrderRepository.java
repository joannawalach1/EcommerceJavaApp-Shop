package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Order;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public List<Order> findOrdersByUserId(Long userId) {
        String jpql = "SELECT o FROM Order o WHERE o.user.id = :userId";
        TypedQuery<Order> query = entityManager.createQuery(jpql, Order.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<Order> findOrdersById(UUID orderId) {
        String jpql = "SELECT o FROM Order o WHERE o.id = :orderId";
        TypedQuery<Order> query = entityManager.createQuery(jpql, Order.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    public Order saveOrder(Order order) {
        entityManager.persist(order);
        return order;
    }

    public boolean delete(UUID orderId) {
        Query query = entityManager.createQuery("DELETE FROM Order p WHERE p.id = :orderId");
        query.setParameter("orderId", orderId);
        return true;
    }

    public List<Order> findAllOrders() {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o", Order.class);
        return query.getResultList();
    }
}
