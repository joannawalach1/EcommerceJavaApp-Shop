package pl.com.coders.shop2.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = createSampleCategory();
        categoryRepository.save(category);
        product = createSampleProduct(category);
        productRepository.add(product);
    }

    @Test
    void shouldAddProductToRepositoryAndGenerateId() {
        Product product = createSampleProduct(category);
        productRepository.add(product);
        assertThat(product).isNotNull();
        assertThat(product.getName()).isNotNull();
    }

    @Test
    void shouldGetProductFromRepository() {
        List<Product> foundProducts = productRepository.getProductsByCategory(category);
        assertThat(foundProducts).isNotNull();
    }

    @Test
    void shouldDeleteProductFromRepository() {
        Product existingProduct = productRepository.add(product);

        boolean deleteResult = productRepository.delete(existingProduct.getId());
        assertTrue(deleteResult);

    }

    @Test
    void shouldUpdateProductInRepository() {
        Category newCategory = createSampleCategory();
        categoryRepository.save(newCategory);
        Product newProduct = createSampleProduct(newCategory);
        newProduct.setCategory(newCategory);
        Product addedProduct = productRepository.add(newProduct);
        LocalDateTime expectedCreated = addedProduct.getCreated().withNano(0);
        addedProduct.setName("Updated Product");
        addedProduct.setPrice(BigDecimal.valueOf(29.99));
        Product updatedProduct = productRepository.update(addedProduct);
        LocalDateTime actualCreated = updatedProduct.getCreated().withNano(0);
        assertThat(actualCreated).isEqualTo(expectedCreated);
    }

    @Test
    void shouldFindAllProductsInRepository() {
        Product product2 = createSampleProduct(category);
        productRepository.add(product2);
        List<Product> allProducts = productRepository.findAll();
        assertEquals(29, allProducts.size());
    }

    private Product createSampleProduct(Category category) {
        return Product.builder()
                .name("Sample Product")
                .description("Sample Description")
                .price(BigDecimal.valueOf(19.99))
                .quantity(10)
                .category(category)
                .build();
    }

    private Category createSampleCategory() {
        return Category.builder()
                .name("Electronics")
                .build();
    }
}
