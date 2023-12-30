package pl.com.coders.shop2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);
    @Mapping(source = "product", target = "productTitle", qualifiedByName = "mapProductToProductTitle")
    CartLineItemDto cartLineItemToDto(CartLineItem cartLineItem);

    @Named("mapProductToProductTitle")
    static String mapProductToProductTitle(Product product) {
        return product.getName();
    }

    CartLineItem dtoToCartLineItem(CartLineItemDto cartLineItemDto);
}
