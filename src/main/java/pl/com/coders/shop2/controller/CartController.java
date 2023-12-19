package pl.com.coders.shop2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;
import pl.com.coders.shop2.exceptions.CartLineItemCreationException;
import pl.com.coders.shop2.exceptions.EmptyCartException;
import pl.com.coders.shop2.exceptions.ProductNotFoundException;
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

    @PostMapping("/addOrUpdateCartItem")
    public ResponseEntity<CartLineItemDto> addOrUpdateCartItem(
            @RequestParam String userEmail,
            @RequestParam String productTitle,
            @RequestParam BigDecimal amount) throws EmptyCartException, CartLineItemCreationException, ProductNotFoundException {
        CartLineItemDto cartItemDto = cartService.addOrUpdateCartItem(userEmail, productTitle, amount);
        return ResponseEntity.ok(cartItemDto);
    }

    @GetMapping("/cart/{userId}")
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long cartId) {
        cartService.delete(cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAll() {
        cartService.deleteAll();
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/deleteByCartIndex")
    public ResponseEntity<String> deleteByCartIndex(@RequestParam Long cartId, @RequestParam int cartIndex) {
        cartService.getCartByCartId(cartId);
        cartService.deleteByCartIndex(cartId, cartIndex);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/getOrder")
    public ResponseEntity<List<CartDto>> findAllCarts() {
        List<CartDto> allCarts = cartService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(allCarts);
    }
    @PutMapping("/updateCartByCartId")
    public ResponseEntity<CartDto> updateCartByCartId(@RequestBody CartDto cartDto) {
            CartDto updatedCartDto = cartService.updateCartByCartId(cartDto);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCartDto);
        }
    }



