package pl.com.coders.shop2.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_line_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cart cart;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Product product;

    private int cartLineQuantity;
    private BigDecimal cartLinePrice;
    @Column(name = "cartIndex")
    private int cartIndex;

}
