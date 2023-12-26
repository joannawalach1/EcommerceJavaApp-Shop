package pl.com.coders.shop2.helpers;


import org.springframework.stereotype.Component;
import pl.com.coders.shop2.domain.CartLineItem;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class CartTotalCalculator {

        public static BigDecimal calculateCartLineItemTotal(CartLineItem item) {
            return item.getCartLinePrice().multiply(BigDecimal.valueOf(item.getCartLineQuantity()));
        }

        public static BigDecimal calculateTotalPriceForCart(Set<CartLineItem> cartItems) {
            return cartItems.stream()
                    .map(CartTotalCalculator::calculateCartLineItemTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
}
