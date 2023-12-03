package pl.com.coders.shop2.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
import pl.com.coders.shop2.domain.Order;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.repository.OrderRepository;
import pl.com.coders.shop2.repository.UserRepository;
import pl.com.coders.shop2.service.OrderService;
import pl.com.coders.shop2.service.ProductService;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderService orderService;

    private List<Order> allOrders;

    @BeforeEach void setUp() {
        allOrders = prepareTestData();
    }

    @Test
    void getOrdersByUserId() throws Exception {
        Long userId = 1L;
        when(orderService.findOrdersByUserId(any())).thenReturn(allOrders);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/orders/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<Order> responseOrders = objectMapper.readValue(jsonResponse, new TypeReference<List<Order>>() {
        });
        assertEquals(allOrders.size(), responseOrders.size());
        verify(orderService, times(1)).findOrdersByUserId(userId);
    }

    @Test
    void findOrdersByOrderId() throws Exception {
        UUID orderId = UUID.fromString("a0a1ab07-d158-4e89-8b42-2fd8c677147f");
        when(orderService.findOrdersByUserId(any())).thenReturn(allOrders);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<Order> responseOrders = objectMapper.readValue(jsonResponse, new TypeReference<List<Order>>() {
        });
        verify(orderService, times(1)).findOrdersById(any());
    }

    @Test
    void saveOrder() {
    }

    @Test
    void delete() throws Exception {
        UUID orderId = UUID.fromString("a0a1ab07-d158-4e89-8b42-2fd8c677147f");
        when(orderService.delete(any())).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/{id}", orderId))
                .andExpect(status().isNoContent());
        verify(orderService, times(1)).delete(orderId);
    }

    @Test
    void findAllOrders() throws Exception {
        when(orderService.findAllOrders()).thenReturn(allOrders);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/orders/getOrder"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(allOrders)))
                .andReturn();

        assertNotNull(result);
        verify(orderService, Mockito.times(1)).findAllOrders();
    }

    private List<Order> prepareTestData() {
        User user1 = userRepository.create(new User(1L, "john@example.com", "John", "Doe", "pass1"));
        User user2 = userRepository.create(new User(2L, "anne@example.com", "Anne", "Boe", "pass2"));
        Order order1 = orderRepository.saveOrder(new Order(user1, 100.0));
        Order order2 = orderRepository.saveOrder(new Order(user2, 50));
        return Arrays.asList(order1, order2);
    }
}