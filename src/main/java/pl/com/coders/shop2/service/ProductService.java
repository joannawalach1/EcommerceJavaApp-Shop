package pl.com.coders.shop2.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.ProductDto;
import pl.com.coders.shop2.repository.ProductRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public ProductDto create(ProductDto productDto) {
        return productRepository.add(productDto);
    }
    public ProductDto get(Long id) {
        return productRepository.getProductById(id);
    }
    public boolean delete(Long id) {
        return productRepository.delete(id);
    }
    public ProductDto update(ProductDto productDto, Long id) {
        return productRepository.update(productDto);
    }

    public List<ProductDto> getAll() {return productRepository.findAll();}
    public List<Product> getAllProd() {return productRepository.findAllProd();}

}