package pl.com.coders.shop2.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})

@Table(name = "PRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "category_id")
    //@JsonIgnore
    private Category category;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
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
