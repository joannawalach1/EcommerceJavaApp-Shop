package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.dto.ProductDto;
import pl.com.coders.shop2.exceptions.ProductWithGivenIdNotExistsException;
import pl.com.coders.shop2.exceptions.ProductWithGivenTitleExistsException;
import pl.com.coders.shop2.exceptions.ProductWithGivenTitleNotExistsException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public ProductRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(rollbackFor = ProductWithGivenTitleExistsException.class)
    public Product add(Product product) throws ProductWithGivenTitleExistsException {
        if (getProductByName(product.getName()).isPresent()) {
            throw new ProductWithGivenTitleExistsException("Product with the given title already exists.");
        }
        if (product.getName() == null) {
            throw new ProductWithGivenTitleNotExistsException("Product with the given title doesn't exist");
        }
        return entityManager.merge(product);
    }

    public Product getProductById(Long id) throws ProductWithGivenIdNotExistsException {
        Product product = entityManager.find(Product.class, id);
        if (product == null) {
            throw new ProductWithGivenIdNotExistsException("Product with the given Id " + id + " doesn't exist");
        }
        return product;
    }


    public List<Product> getProductsByCategory(Category category) {
        String jpql = "SELECT p FROM Product p WHERE p.category = :category";
        return entityManager.createQuery(jpql, Product.class)
                .setParameter("category", category)
                .getResultList();
    }


    @Transactional
    public boolean delete(Long id) {
        Query query = entityManager.createQuery("DELETE FROM Product p WHERE p.id = :productId");
        query.setParameter("productId", id);

        int deletedCount = query.executeUpdate();
        return deletedCount > 0;
    }

    @Transactional
    public Product update(Product product, Long id) throws ProductWithGivenIdNotExistsException {
        int updatedEntities = entityManager.createQuery(
                        "UPDATE Product p SET p.name = :name, p.description = :description, p.price = :price, p.quantity = :quantity WHERE p.id = :id")
                .setParameter("name", product.getName())
                .setParameter("description", product.getDescription())
                .setParameter("price", product.getPrice())
                .setParameter("quantity", product.getQuantity())
                .setParameter("id", id)
                .executeUpdate();

        if (updatedEntities == 0) {
            throw new ProductWithGivenIdNotExistsException("Product with the given ID does not exist.");
        }

        return entityManager.find(Product.class, id);
    }

    @Transactional
    public List<Product> findAllProd() {
        return entityManager.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
    }

    @Transactional
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Product").executeUpdate();
    }

    private Optional<Product> getProductByName(String name) {
        String jpql = "SELECT p FROM Product p WHERE p.name = :name";
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class)
                .setParameter("name", name);
        return query.getResultStream().findFirst();
    }
}