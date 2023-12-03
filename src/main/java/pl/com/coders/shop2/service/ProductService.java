package pl.com.coders.shop2.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.dto.ProductDto;
import pl.com.coders.shop2.mapper.ProductMapper;
import pl.com.coders.shop2.repository.CategoryRepository;
import pl.com.coders.shop2.repository.OrderLineItemRepository;
import pl.com.coders.shop2.repository.ProductRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private ProductMapper productMapper;
    private CategoryRepository categoryRepository;
    private OrderLineItemRepository orderLineItemRepository;

    public ProductDto create(ProductDto productDto) {
        Long categoryId = productDto.getCategoryType().getId();
        System.out.println("Category ID: " + categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));

        Product product = productMapper.dtoToProduct(productDto);
        product.setCategory(category);

        Product savedProduct = productRepository.add(product);
        return productMapper.productToDto(savedProduct);
    }

    public ProductDto get(Long id) {
        ProductDto productDto = productMapper.productToDto(productRepository.getProductById(id));
        return productDto;
    }

    public boolean delete(Long id) {
        orderLineItemRepository.deleteByProductId(id);
        productRepository.delete(id);
        return true;
    }

    public ProductDto update(ProductDto productDto, Long id) {
        Product product = productMapper.dtoToProduct(productDto);
        Product update = productRepository.update(product, id);
        return productMapper.productToDto(update);
    }

    public List<Product> getAllProd() {
        return productRepository.findAllProd();
    }

}