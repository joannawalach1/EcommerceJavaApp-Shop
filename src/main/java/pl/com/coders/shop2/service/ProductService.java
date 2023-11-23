package pl.com.coders.shop2.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.ProductDto;
import pl.com.coders.shop2.mapper.ProductMapper;
import pl.com.coders.shop2.repository.CategoryRepository;
import pl.com.coders.shop2.repository.ProductRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private ProductMapper productMapper;
    private CategoryRepository categoryRepository;
    public ProductDto create(ProductDto productDto) {
        Long id = productDto.getCategoryType().getId();
        Product product = productMapper.dtoToProduct(productDto);
        product.setCategory(categoryRepository.getCategoryById(id));
        Product add = productRepository.add(product);
        return productMapper.productToDto(add);
    }
    public ProductDto get(Long id) {
        ProductDto productDto = productMapper.productToDto(productRepository.getProductById(id));
        return productDto;
    }
    public boolean delete(Long id) {
        return productRepository.delete(id);
    }
    public ProductDto update(ProductDto productDto, Long id) {
        Product product = productMapper.dtoToProduct(productDto);
        Product update = productRepository.update(product, id);
        return productMapper.productToDto(update);
    }

    public List<ProductDto> getAll() {return productRepository.findAll();}
    public List<Product> getAllProd() {return productRepository.findAllProd();}

}