package pl.com.coders.shop2.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.com.coders.shop2.domain.Category;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.ProductDto;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "category.name", target = "categoryType")
    ProductDto productToDto(Product product);

    @Mapping(source = "categoryType", target = "category.name")
    Product dtoToProduct(ProductDto productDto);
}
