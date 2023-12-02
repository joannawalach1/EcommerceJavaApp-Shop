package pl.com.coders.shop2.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_line_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart_Line_Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int cartLineQuantity;
    private BigDecimal cartLinePrice;
    @Column(name = "cart_index")
    private Integer cartIndex;
}
