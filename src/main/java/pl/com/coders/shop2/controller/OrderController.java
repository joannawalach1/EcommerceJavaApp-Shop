package pl.com.coders.shop2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.coders.shop2.domain.Order;
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

    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@PathVariable Long id) {
        List<OrderDto> orderByUserId = orderService.findOrdersByUserId(id);
        return ResponseEntity.status(HttpStatus.OK).body(orderByUserId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<OrderDto>> findOrderByOrderId(@PathVariable UUID id) {
        List<OrderDto> ordersByOrderId = orderService.findOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ordersByOrderId);
    }

    @PostMapping
    public ResponseEntity<OrderDto> saveOrder(@RequestBody OrderDto orderDto) {
        OrderDto createdOrder = orderService.saveOrder(orderDto);
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
