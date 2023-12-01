package pl.com.coders.shop2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.coders.shop2.domain.Order;
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

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long id) {
        List<Order> ordersByUserId = orderService.findOrdersByUserId(id);
        return ResponseEntity.status(HttpStatus.OK).body(ordersByUserId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Order>> findOrdersByOrderId(@PathVariable UUID id) {
        List<Order> ordersByOrderId = orderService.findOrdersById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ordersByOrderId);
    }

    @PostMapping
    public ResponseEntity<Order> saveOrder(@RequestBody Order order) {
        Order createdOrder = orderService.saveOrder(order);
        return ResponseEntity.status(HttpStatus.OK).body(createdOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getOrder")
    public ResponseEntity<List<Order>> findAllOrders() {
        List<Order> allOrders = orderService.findAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(allOrders);
    }
}
