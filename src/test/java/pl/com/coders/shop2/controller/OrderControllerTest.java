//package pl.com.coders.shop2.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import pl.com.coders.shop2.domain.dto.OrderDto;
//import pl.com.coders.shop2.service.OrderService;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//import java.util.List;
//import java.util.UUID;
//
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test")
//@AutoConfigureMockMvc
//class OrderControllerTest {
//
//    @Mock
//    private OrderService orderService;
//
//    @InjectMocks
//    private OrderController orderController;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void testGetOrdersByUserId() throws Exception {
//        Long userId = 1L;
//        List<OrderDto> orders = Collections.singletonList(new OrderDto());
//
//        Mockito.when(orderService.findOrdersByUserId(userId)).thenReturn(orders);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/orders/user/{id}", userId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(orders.size()));
//    }
//
//    @Test
//    void testFindOrderByOrderId() throws Exception {
//        UUID orderId = UUID.fromString("8ee0c4f6-1b02-4a80-bf8b-ba70f5d9f49d");
//        List<OrderDto> orders = Collections.singletonList(new OrderDto());
//
//        Mockito.when(orderService.findOrderById(orderId)).thenReturn(orders);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", orderId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(orders.size()));
//    }
//
//    @Test
//    void testSaveOrder() throws Exception {
//        OrderDto orderDto = new OrderDto();
//        orderDto.setUserLastName("Doe");
//        orderDto.setTotalAmount(BigDecimal.valueOf(100.0));
//
//        Mockito.when(orderService.saveOrder(orderDto)).thenReturn(orderDto);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderDto)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.userLastName").value(orderDto.getUserLastName()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.totalAmount").value(orderDto.getTotalAmount().toString()));
//    }
//
////    @Test
////    void testDelete() throws Exception {
////        UUID orderId = UUID.fromString("8ee0c4f6-1b02-4a80-bf8b-ba70f5d9f49d");
////
////        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/delete/{id}", orderId)
////                        .contentType(MediaType.APPLICATION_JSON))
////                .andExpect(MockMvcResultMatchers.status().isNoContent());
////
////        Mockito.verify(orderService, Mockito.times(1)).delete(orderId);
////    }
//
//    @Test
//    void testFindAllOrders() throws Exception {
//        List<OrderDto> orders = Collections.singletonList(new OrderDto());
//
//        Mockito.when(orderService.findAllOrders()).thenReturn(orders);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/orders/getOrder")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//}
