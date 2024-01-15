package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
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
    public Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus("nowy");
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderLineItems(new HashSet<>());
        order.setCreated(LocalDateTime.now());
        order.setCreated(LocalDateTime.now());
        entityManager.persist(order);
        return order;
    }

    @Transactional
    public OrderLineItem createOrderLineItem(Order order, Product product, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrder(order);
        orderLineItem.setProduct(product);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        order.addOrderLineItems(orderLineItem);
        entityManager.persist(orderLineItem);
        return orderLineItem;
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

    @Transactional
    public List<Order> findAllOrders() {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o", Order.class);
        return query.getResultList();
    }
}

