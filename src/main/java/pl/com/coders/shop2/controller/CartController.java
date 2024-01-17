package pl.com.coders.shop2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.exceptions.ProductNotFoundException;
import pl.com.coders.shop2.mapper.CartMapper;
import pl.com.coders.shop2.service.CartService;
import pl.com.coders.shop2.service.UserService;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final CartMapper cartMapper;

    public CartController(CartService cartService, UserService userService, CartMapper cartMapper) {
        this.cartService = cartService;
        this.userService = userService;
        this.cartMapper = cartMapper;
    }
    @PostMapping("/addProductToCart")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CartDto> addProductToCart(
            @RequestParam String userEmail,
            @RequestParam String productTitle,
            @RequestParam int amount) throws ProductNotFoundException {
        CartDto cartDto = cartService.addProductToCart(userEmail, productTitle, amount);
        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }

    @DeleteMapping("/{cartIndex}/{cartId}")
    public ResponseEntity<String> deleteCartByIndex(@PathVariable int cartIndex, @PathVariable Long cartId) {
        cartService.deleteByIndex(cartId, cartIndex);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getCarts")
    public ResponseEntity<CartDto> getCartsForAuthUser() {
        CartDto cartForAuthUser = cartService.getCartForAuthUser();
        return  ResponseEntity.status(HttpStatus.OK).body(cartForAuthUser);
    }

}



