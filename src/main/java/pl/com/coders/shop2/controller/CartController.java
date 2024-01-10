package pl.com.coders.shop2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.exceptions.ProductNotFoundException;
import pl.com.coders.shop2.service.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/addProductToCart")
    public ResponseEntity<CartDto> addProductToCart(
            @RequestParam String userEmail,
            @RequestParam String productTitle,
            @RequestParam int amount) throws ProductNotFoundException {
        CartDto cart = cartService.addProductToCart(userEmail, productTitle, amount);
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

    @DeleteMapping("/{cartIndex}/{cartId}")
    public ResponseEntity<String> deleteCartByIndex( @PathVariable int cartIndex, @PathVariable Long cartId) {
        cartService.deleteByIndex(cartId, cartIndex);
        return ResponseEntity.noContent().build();
    }
}



