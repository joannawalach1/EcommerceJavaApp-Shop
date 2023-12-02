package pl.com.coders.shop2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.dto.CartDto;
import pl.com.coders.shop2.domain.dto.ProductDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

  //  @Mapping(source = "category.name", target = "categoryType")
    CartDto cartToDto(Cart cart);

  //  @Mapping(source = "categoryType", target = "category.name")
    Cart dtoToCart(CartDto cartdto);

    List<CartDto> cartToDto(List<Cart> carts);
}
