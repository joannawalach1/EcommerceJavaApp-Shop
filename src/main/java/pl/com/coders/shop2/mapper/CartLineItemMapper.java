package pl.com.coders.shop2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pl.com.coders.shop2.domain.Cart;
import pl.com.coders.shop2.domain.Cart_Line_Item;
import pl.com.coders.shop2.domain.Product;
import pl.com.coders.shop2.domain.dto.CartLineItemDto;
import pl.com.coders.shop2.domain.dto.ProductDto;

@Mapper(componentModel = "spring")
public interface CartLineItemMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "cartLineItem", target = "productTitle", qualifiedByName = "mapCartLineItemToProductTitle")
    CartLineItemDto cartLineItemToDto(Cart_Line_Item cartLineItem);

   // @Mapping(source = "categoryType", target = "category.name")
    Cart_Line_Item dtoToCartLineItem(CartLineItemDto cartLineItemDto);

    @Named("mapCartLineItemToProductTitle")
    default String mapCartLineItemToProductTitle(Cart_Line_Item cartLineItem) {
        return cartLineItem.getProduct().getName();
    }
}
