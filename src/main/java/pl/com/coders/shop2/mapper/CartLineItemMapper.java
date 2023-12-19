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

        @Mapping(source = "cartLineItem.product.name", target = "productTitle")
        CartLineItemDto cartLineItemToDto(CartLineItem cartLineItem);

        CartLineItem dtoToCartLineItem(CartLineItemDto cartLineItemDto);
    }


