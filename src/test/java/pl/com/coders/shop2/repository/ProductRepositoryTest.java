package pl.com.coders.shop2.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.CategoryType;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.dto.ProductDto;
import pl.com.coders.shop2.exceptions.ProductWithGivenIdNotExistsException;
import pl.com.coders.shop2.exceptions.ProductWithGivenTitleExistsException;
import pl.com.coders.shop2.exceptions.ProductWithGivenTitleNotExistsException;

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

    private CategoryType categoryType;


    @BeforeEach
    void setUp() {
        category = createSampleCategory();
        Category save = categoryRepository.save(category);
        product = createSampleProduct(save);
    }

    @Test
    void shouldAddProductToRepositoryAndGenerateId() {
        assertThat(product).isNotNull();
        assertThat(product.getName()).isNotNull();
    }

//    @Test
//    void shouldGetProductFromRepositoryById() {
//        Long productId = 1L;
//        Product productById = productRepository.getProductById(productId);
//        assertNotNull(productById.getId());
//    }

//    @Test
//    void shouldGetProductFromRepository() {
//        List<Product> foundProducts = productRepository.getProductsByCategory(category);
//        assertThat(foundProducts).isNotNull();
//    }
//
//    @Test
//    void shouldDeleteProductFromRepository() {
//        Long productIdToDelete = 1L;
//        boolean deleteResult = productRepository.delete(productIdToDelete);
//        assertTrue(deleteResult);
//    }
//
//    @Test
//    void shouldUpdateProductInRepository() {
//        category = categoryRepository.getCategoryById(1L);
//        Product exitingProduct = productRepository.add(new Product(category, "Product15", "Description11", BigDecimal.valueOf(200), 50));
//        Product newProduct = new Product(category, "Product15", "Description13", BigDecimal.valueOf(500), 5);
//
//        Product updatedProduct = productRepository.update(newProduct, exitingProduct.getId());
//        assertNotNull(updatedProduct);
//        assertEquals(exitingProduct.getId(), updatedProduct.getId());
//    }

    @Test
    void shouldFindAllProductsInRepository() {
        List<Product> allProducts = productRepository.findAllProd();
        assertEquals(2, allProducts.size());
    }

    @Test
    void add_WithNullProductName_ShouldThrowException() {
        Product productWithNullName = new Product(category, null, "Description13", BigDecimal.valueOf(200), 50);
        assertThrows(ProductWithGivenTitleNotExistsException.class, () -> productRepository.add(productWithNullName));
    }

    @Test
    void getProductById_WithNonExistingId_ShouldThrowException() {
        Long nonExistingId = 1200L;

        ProductWithGivenIdNotExistsException exception = assertThrows(ProductWithGivenIdNotExistsException.class, () -> {
            productRepository.getProductById(nonExistingId);
        });

        assertEquals("Product with the given Id 1200 doesn't exist", exception.getMessage());
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
                .name("Books")
                .build();
    }

}

