package pl.com.coders.shop2.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "order_line_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;
    private int quantity;
}
