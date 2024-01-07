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

    @PostMapping("/addProductToCart")
    public ResponseEntity<CartLineItemDto> addProductToCart(
            @RequestParam String userEmail,
            @RequestParam String productTitle,
            @RequestParam int amount) throws ProductNotFoundException {
        CartLineItemDto cartItemDto = cartService.addProductToCart(userEmail, productTitle, amount);
        return ResponseEntity.status(HttpStatus.OK).body(cartItemDto);
    }

    @DeleteMapping("/{cartIndex}")
    public ResponseEntity<String> deleteCartByIndex(@PathVariable int cartIndex ) {
        cartService.deleteByIndex(cartIndex);
        return ResponseEntity.noContent().build();
    }

//    @DeleteMapping("/removeEmptyCarts")
//    public ResponseEntity<Void> removeEmptyCarts() {
//            cartService.removeEmptyCarts();
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}



