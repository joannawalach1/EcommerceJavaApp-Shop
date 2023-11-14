package pl.com.coders.shop2.controller;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.service.ProductService;
@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    @PostMapping
    public Product create(@RequestBody Product product, Category category) {
        return productService.create(product);
    }
    @GetMapping("/{id}")
    public Product get(@PathVariable Long id) {
        return productService.get(id);
    }
    @DeleteMapping
    public boolean delete(@RequestParam Long id) {
        return productService.delete(id);
    }
    @PutMapping("/{id}")
    public Product update(@RequestBody Product product, @PathVariable Long id ) {
        return productService.update(product, id);
    }
}