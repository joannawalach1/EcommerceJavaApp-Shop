package pl.com.coders.shop2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.repository.ProductRepository;
import pl.com.coders.shop2.service.ProductService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;
    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        category = createSampleCategory();
        product = createSampleProduct(category);
        when(productService.create(product)).thenReturn(product);
    }

    @Test
    void create() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Product responseProduct = objectMapper.readValue(responseContent, Product.class);

        assertEquals(product, responseProduct);
    }

    @Test
    void get() throws Exception {
        Long productId = 1L;
        when(productService.get((productId))).thenReturn(product);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        Product responseProduct = objectMapper.readValue(json, Product.class);
        assertEquals(product, responseProduct);
    }


    @Test
    void delete() throws Exception {
        long productId = 1L;
        when(productService.delete(productId)).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/product")
                        .param("id", String.valueOf(productId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        assertNotNull(mvcResult);
    }

    @Test
    void update() throws Exception {
        Long productId = 1L;
        Product updatedProduct = createSampleProduct(category);
        String json = objectMapper.writeValueAsString(updatedProduct);

        when(productService.update(updatedProduct, productId)).thenReturn(updatedProduct);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/product/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Product responseProduct = objectMapper.readValue(responseContent, Product.class);
        assertEquals(updatedProduct, responseProduct);
    }


    private Product createSampleProduct(Category category) {
        return Product.builder()
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