package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.ProductDto;
import pl.com.coders.shop2.exceptions.ProductWithGivenIdNotExistsException;
import pl.com.coders.shop2.exceptions.ProductWithGivenTitleExistsException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private List<Product> products;
    @PersistenceContext
    private EntityManager entityManager;

    public ProductRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public ProductDto add(ProductDto product) throws ProductWithGivenTitleExistsException {
        if (getProductByName(product.getName()).isPresent()) {
            throw new ProductWithGivenTitleExistsException("message");
        }
        return entityManager.merge(product);
    }

    public ProductDto getProductById(Long id) throws ProductWithGivenIdNotExistsException {
        ProductDto productDto = entityManager.find(ProductDto.class, id);
        if (productDto == null) {
            throw new ProductWithGivenIdNotExistsException("Product with the given Id " + id + " doesn't exist");
        }
        return productDto;
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
        if (product != null) {
            entityManager.remove(product);
            return true;
        }
        return false;
    }


    @Transactional
    public ProductDto update(ProductDto product) throws ProductWithGivenIdNotExistsException {
        if (product.getId() == null || entityManager.find(Product.class, product.getId()) == null) {
            throw new ProductWithGivenIdNotExistsException("Product with the given ID does not exist.");
        }
        return entityManager.merge(product);
    }

    @Transactional
    public List<ProductDto> findAll() {
        return entityManager.createQuery("SELECT p FROM ProductDto p", ProductDto.class)
                .getResultList();
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
        Optional<Product> first = query.getResultStream().findFirst();
        return first;
    }
}