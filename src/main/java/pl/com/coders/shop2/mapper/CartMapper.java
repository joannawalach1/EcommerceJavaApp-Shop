package pl.com.coders.shop2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.dto.CartDto;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(source = "cart", target = "userName", qualifiedByName = "mapCartToUserName")
    CartDto cartToDto(Cart cart);

    //  @Mapping(source = "categoryType", target = "category.name")
    Cart dtoToCart(CartDto cartdto);

    CartDto cartToDto(Optional<Cart> carts);

    @Named("mapCartToUserName")
    default String mapCartToUserName(Cart cart) {
        return cart.getUser().getEmail();
    }

    List<CartDto> cartToDto(List<CartDto> carts);
}
