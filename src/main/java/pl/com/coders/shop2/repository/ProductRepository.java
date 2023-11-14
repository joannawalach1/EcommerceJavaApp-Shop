package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.Product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public ProductRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Product add(Product product) {
        return entityManager.merge(product);
    }

    public Product getProductById(Long id) {
        return entityManager.find(Product.class, id);
    }

    public List<Product> getProductsByCategory(Category category) {
        String jpql = "SELECT p FROM Product p WHERE p.category = :category";
        return entityManager.createQuery(jpql, Product.class)
                .setParameter("category", category)
                .getResultList();
    }

    @Transactional
    public boolean delete(Long id) {
        Product product = entityManager.find(Product.class, id);
        entityManager.remove(product);
        return true;
    }


    @Transactional
    public Product update(Product product) {
        entityManager.merge(product);
        return product;
    }

    public List<Product> findAll() {
        return entityManager.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }

    @Transactional
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Product").executeUpdate();
    }

}