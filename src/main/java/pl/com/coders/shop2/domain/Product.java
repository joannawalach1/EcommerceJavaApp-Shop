package pl.com.coders.shop2.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private Category category;

    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;

    @OneToMany(mappedBy = "product")
    private Set<OrderLineItem> orderLineItems;

    @OneToMany(mappedBy = "product")
    private Set<CartLineItem> cartLineItem;

    @CreationTimestamp
    private LocalDateTime created;

    @CreationTimestamp
    private LocalDateTime updated;

    public Product(Category category, String name, String description, BigDecimal price, int quantity) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + "category" + category + "'name='" + name + ", description=" + description + ", price=" + price + ", quantity=" + quantity + ", created=" + created + ", updated=" + updated + "}";
    }
}
