package pl.com.coders.shop2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.repository.ProductRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product inputProduct;
    private Long categoryId;

    @BeforeEach
    void setUp() {
        category = createSampleCategory();
        inputProduct = createSampleProduct(category);
        categoryId = category.getId();
    }

    @Test
    void create() {
        when(productRepository.add(inputProduct)).thenReturn(inputProduct);
        Product createdProduct = productService.create(inputProduct);
        assertNotNull(createdProduct);
    }

    @Test
    void get() {
        when(productRepository.getProductById(inputProduct.getId())).thenReturn(inputProduct);
        Product resultProduct = productService.get(inputProduct.getId());
        assertNotNull(resultProduct);
        assertSame(inputProduct, resultProduct);
        verify(productRepository, times(1)).getProductById(inputProduct.getId());
    }

    @Test
    void delete() {
        when(productRepository.delete(inputProduct.getId())).thenReturn(true);
        boolean resultProduct = productService.delete(inputProduct.getId());
        assertTrue(resultProduct);
        verify(productRepository, times(1)).delete(inputProduct.getId());
    }

    @Test
    void update() {
        Long productId = inputProduct.getId();
        when(productRepository.update(inputProduct)).thenReturn(inputProduct);
        Product updatedProduct = productService.update(inputProduct, productId);

        assertNotNull(updatedProduct);
        assertSame(inputProduct, updatedProduct);
        verify(productRepository, times(1)).update(inputProduct);
    }


    private Product createSampleProduct(Category category) {
        return Product.builder()
                .category(category)
                .name("Sample Product")
                .description("Sample Description")
                .price(BigDecimal.valueOf(19.99))
                .quantity(10)
                .build();
    }

    private Category createSampleCategory() {
        return Category.builder()
                .name("Books")
                .build();
    }
}
