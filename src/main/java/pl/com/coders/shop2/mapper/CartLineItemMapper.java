package pl.com.coders.shop2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pl.com.coders.shop2.domain.CartLineItem;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;

@Mapper(componentModel = "spring")
public interface CartLineItemMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "cartLineItem", target = "productTitle", qualifiedByName = "mapCartLineItemToProductTitle")
    CartLineItemDto cartLineItemToDto(CartLineItem cartLineItem);

   // @Mapping(source = "categoryType", target = "category.name")
    CartLineItem dtoToCartLineItem(CartLineItemDto cartLineItemDto);

    @Named("mapCartLineItemToProductTitle")
    default String mapCartLineItemToProductTitle(CartLineItem cartLineItem) {
        return cartLineItem.getProduct().getName();
    }
}
