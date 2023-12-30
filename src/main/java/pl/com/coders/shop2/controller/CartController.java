package pl.com.coders.shop2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;
import pl.com.coders.shop2.exceptions.ProductNotFoundException;
import pl.com.coders.shop2.service.CartService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/addOrUpdateCartItem")
    public ResponseEntity<CartLineItemDto> addToCart(
            @RequestParam String userEmail,
            @RequestParam String productTitle,
            @RequestParam int amount) throws ProductNotFoundException {
        CartLineItemDto cartItemDto = cartService.addProductToCart(userEmail, productTitle, amount);
        return ResponseEntity.status(HttpStatus.OK).body(cartItemDto);
    }

    @DeleteMapping("/deleteByCartIndex")
    public ResponseEntity<String> deleteByCartIndex(@RequestParam int cartIndex) {
        if (cartIndexExists(cartIndex)) {
            cartService.deleteByCartIndex(cartIndex);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("Cart with index " + cartIndex + " does not exist.");
        }
    }

    private boolean cartIndexExists(int cartIndex) {
        try {
            CartLineItem cartLineItem = cartService.getCartByIndex(cartIndex);
            return cartLineItem != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}



