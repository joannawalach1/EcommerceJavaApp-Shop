package pl.com.coders.shop2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;
import pl.com.coders.shop2.repository.CartRepository;
import pl.com.coders.shop2.repository.ProductRepository;
import pl.com.coders.shop2.service.CartService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartController(CartService cartService, CartRepository cartRepository, ProductRepository productRepository) {
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/saveCart/{productTitle}/{userEmail}")
    public ResponseEntity<CartDto> saveCart(@PathVariable String productTitle, @PathVariable String userEmail){
        CartDto savedCart = cartService.saveCart(productTitle, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCart);
    }

    @PostMapping("/{productTitle}/{amount}/addToCart")
    public ResponseEntity<CartLineItemDto> addCartItem(@RequestParam String userEmail, @RequestParam String productTitle, @RequestParam BigDecimal amount) {
        CartLineItemDto cartItemDto = cartService.saveCartItem(userEmail, productTitle, amount);
        Optional<Product> product = productRepository.getProductByName(productTitle);
        if (cartItemDto != null) {
            cartItemDto.setProductTitle(productTitle);
            cartItemDto.setCartLineQuantity(cartItemDto.getCartLineQuantity() + amount.intValue());
            cartItemDto.setCartLinePrice(product.get().getPrice().multiply(amount));
            cartItemDto.setCartIndex(cartItemDto.getCartIndex());
            return ResponseEntity.status(HttpStatus.OK).body(cartItemDto);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    @DeleteMapping("/deleteByCartIndex")
    public ResponseEntity<String> deleteByCartIndex(@RequestParam Long cartId, @RequestParam int cartIndex) {
        cartRepository.getCartByCartId(cartId);
        cartRepository.deleteByCartIndex(cartId, cartIndex);
        return ResponseEntity.noContent().build();
    }
}


