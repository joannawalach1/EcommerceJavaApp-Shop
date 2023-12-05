package pl.com.coders.shop2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.service.CartService;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart/{id}")
    public ResponseEntity<CartDto> getCartsByUserId(@PathVariable Long userId) {
        CartDto cartsByUserId = cartService.getCartByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(cartsByUserId);
    }

    @GetMapping("/cart/byId/{id}")
    public ResponseEntity<CartDto> findCartByCartId(@RequestParam Long cartId) {
        CartDto cartsByCartId = cartService.getCartByCartId(cartId);
        return ResponseEntity.status(HttpStatus.OK).body(cartsByCartId);
    }

    @GetMapping("/cart/byEmail")
    public ResponseEntity<CartDto> getCartsByUserEmail(@RequestParam String email) {
        CartDto cartsByUserEmail = cartService.getCartByUserEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(cartsByUserEmail);
    }
    @PostMapping
    public ResponseEntity<CartDto> saveCart(@RequestBody String productTitle, int id, BigDecimal cartLinePrize, String userEmail ) {
        CartDto createdCart = cartService.saveCart(productTitle, id, cartLinePrize, userEmail);
        return ResponseEntity.status(HttpStatus.OK).body(createdCart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long cartId) {
        cartService.delete(cartId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getOrder")
    public ResponseEntity<List<CartDto>> findAllCarts() {
        List<CartDto> allCarts = cartService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(allCarts);
    }

    @PostMapping("/{productTitle}/{cartLineQuantity}/addToCart")
    public CartDto addToCart(@PathVariable String productTitle, @PathVariable int cartLineQuantity, BigDecimal cartLinePrize, String userEmail) {
        return cartService.saveCart(productTitle, cartLineQuantity, cartLinePrize, userEmail);
    }
}


