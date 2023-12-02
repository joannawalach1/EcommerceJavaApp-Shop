package pl.com.coders.shop2.domain.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode()
public class CartLineItemDto {
    private Long cartId;
    private Long productId;
    private int cartLineQuantity;
    private BigDecimal cartLinePrice;
    private Integer cartIndex;
}
