package pl.com.coders.shop2.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.CategoryType;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.exceptions.ProductWithGivenIdNotExistsException;

import java.math.BigDecimal;
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
        product = createSampleProduct(category);
    }

    @Test
    void shouldAddProductToRepositoryAndGenerateId() {
        assertThat(product).isNotNull();
        assertThat(product.getName()).isNotNull();
    }

    @Test
    void shouldGetProductFromRepositoryById() {
        Product add = productRepository.add(product);
        Product productById = productRepository.getProductById(add.getId());
        assertEquals(add.getId(), productById.getId());
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
//        categoryType = categoryType.MOTORYZACJA;
//        Product newProduct = createSampleProduct(categoryType);
//        boolean categoryType = categoryRepository.existsById(1L);
//        Product addedProduct = productRepository.add(newProduct);
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
        List<Product> allProducts = productRepository.findAllProd();
        assertEquals(11, allProducts.size());
    }

//    @Test
//    void add_WithNullProductName_ShouldThrowException() {
//        ProductDto productDto = new ProductDto();
//        assertThrows(ProductWithGivenTitleExistsException.class, () -> productRepository.add(productDto));
//    }

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

