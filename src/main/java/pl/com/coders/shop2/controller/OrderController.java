package pl.com.coders.shop2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.coders.shop2.domain.dto.OrderDto;
import pl.com.coders.shop2.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("saveOrder/{userEmail}/{cartId}")
    public ResponseEntity<OrderDto> saveOrder(@PathVariable String userEmail, @PathVariable Long cartId) {
        OrderDto createdOrder = orderService.createOrderFromCart(userEmail, cartId);
        return ResponseEntity.status(HttpStatus.OK).body(createdOrder);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getOrder")
    public ResponseEntity<List<OrderDto>> findAllOrders() {
        List<OrderDto> allOrders = orderService.findAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(allOrders);
    }
}
