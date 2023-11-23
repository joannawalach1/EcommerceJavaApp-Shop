package pl.com.coders.shop2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.coders.shop2.domain.*;
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

    private CategoryDto categoryDto;

    private CategoryType categoryType;
    private ProductDto inputProductDto;
    private String categoryId;

    @BeforeEach
    void setUp() {
        this.categoryDto = new CategoryDto();
        categoryType = CategoryType.MOTORYZACJA;
        inputProductDto = createSampleDtoProduct(categoryType);
        categoryId = categoryDto.getTitle();
    }

    @Test
    void create() {
        when(productRepository.add(any())).thenReturn(inputProductDto);
        ProductDto createdProduct = productService.create(inputProductDto);
        assertNotNull(createdProduct);
    }

    @Test
    void get() {
        when(productRepository.getProductById(any())).thenReturn(inputProductDto);
        ProductDto resultProduct = productService.get(inputProductDto.getId());
        assertNotNull(resultProduct);
        assertSame(inputProductDto, resultProduct);
        verify(productRepository, times(1)).getProductById(inputProductDto.getId());
    }

    @Test
    void delete() {
        when(productRepository.delete(inputProductDto.getId())).thenReturn(true);
        boolean resultProduct = productService.delete(inputProductDto.getId());
        assertTrue(resultProduct);
        verify(productRepository, times(1)).delete(inputProductDto.getId());
    }

    @Test
    void update() {
        Long productId = inputProductDto.getId();
        when(productRepository.update(inputProductDto)).thenReturn(inputProductDto);
        ProductDto updatedProduct = productService.update(inputProductDto, productId);

        assertNotNull(updatedProduct);
        assertSame(inputProductDto, updatedProduct);
        verify(productRepository, times(1)).update(inputProductDto);
    }

    private ProductDto createSampleDtoProduct(CategoryType categoryType) {
        return ProductDto.builder()
                .categoryType(categoryType)
                .name("Sample Product")
                .description("Sample Description")
                .price(BigDecimal.valueOf(19.99))
                .quantity(10)
                .build();
    }

    private CategoryDto createSampleDtoCategory() {
        return CategoryDto.builder()
                .title("Books")
                .build();
    }
}
