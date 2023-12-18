package pl.com.coders.shop2.domain.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineItemDto {
    private Long orderId;
    private Long productId;
    private int quantity;
}
