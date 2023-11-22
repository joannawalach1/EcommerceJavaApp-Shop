package pl.com.coders.shop2.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.CategoryType;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.ProductDto;
import pl.com.coders.shop2.exceptions.ProductWithGivenIdNotExistsException;
import pl.com.coders.shop2.exceptions.ProductWithGivenTitleExistsException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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
    }

    @Test
    void shouldAddProductToRepositoryAndGenerateId() {
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
        product.setId(1L);
        boolean deleteResult = productRepository.delete(product.getId());
        assertTrue(deleteResult);
    }

//    @Test
//    void shouldUpdateProductInRepository() {
//        Category newCategory = createSampleCategory();
//        Product newProduct = createSampleProduct(newCategory);
//        CategoryType categoryType = categoryRepository.existsById(1L);
//        newProduct.setCategoryType(categoryType);
//        ProductDto addedProduct = productRepository.add(newProduct);
//
//        LocalDateTime expectedCreated = addedProduct.getCreated().withNano(0);
//        addedProduct.setName("Updated Product");
//        addedProduct.setPrice(BigDecimal.valueOf(29.99));
//
//        ProductDto updatedProduct = productRepository.update(addedProduct);
//
//        LocalDateTime actualCreated = updatedProduct.getCreated().withNano(0);
//        assertThat(actualCreated).isEqualTo(expectedCreated);
//
//        assertThat(updatedProduct.getName()).isEqualTo("Updated Product");
//        assertThat(updatedProduct.getPrice()).isEqualTo(BigDecimal.valueOf(29.99));
//    }

    @Test
    void shouldFindAllProductsInRepository() {
        List<ProductDto> allProducts = productRepository.findAll();
        assertEquals(1, allProducts.size());
    }

    @Test
    void add_WithNullProductName_ShouldThrowException() {
        ProductDto productDto = new ProductDto();
        assertThrows(ProductWithGivenTitleExistsException.class, () -> productRepository.add(productDto));
    }

    @Test
    void getProductById_WithNonExistingId_ShouldThrowException() {
        Long nonExistingId = 1L;
        when(productRepository.getProductById(eq(nonExistingId))).thenReturn(null);

        assertThrows(ProductWithGivenIdNotExistsException.class, () -> {
            productRepository.getProductById(nonExistingId);
        });

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

