package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Order;
import pl.com.coders.shop2.domain.OrderLineItem;
import pl.com.coders.shop2.domain.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
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
    public void createOrder(Order order) {
        entityManager.merge(order);
    }

    @Transactional
    public boolean delete(UUID id) {
        Order order = entityManager.find(Order.class, id);
        if (order != null) {
            entityManager.remove(order);
            return true;
        }
        throw new EntityNotFoundException("Order with ID " + id + " not found");
    }

    public List<Order> findAllOrders() {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o", Order.class);
        return query.getResultList();
    }

}

