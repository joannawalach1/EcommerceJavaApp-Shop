package pl.com.coders.shop2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;

import java.util.Optional;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface CartLineItemMapper {

    CartLineItemMapper INSTANCE = Mappers.getMapper(CartLineItemMapper.class);

    @Mapping(source = "cartLineItem", target = "productTitle", qualifiedByName = "mapCartLineItemToProductTitle")
    CartLineItemDto cartLineItemToDto(Optional<CartLineItem> cartLineItem);

    CartLineItem dtoToCartLineItem(CartLineItemDto cartLineItemDto);

    @Named("mapCartLineItemToProductTitle")
    default String mapCartLineItemToProductTitle(Optional<CartLineItem> cartLineItem) {
        return cartLineItem.map(item -> item.getProduct().getName()).orElse(null);
    }
}

