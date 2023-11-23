package pl.com.coders.shop2.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"id"})
@Table(name = "product_dto")
public class ProductDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   // @JsonIgnore
    private CategoryType categoryType;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;

    private LocalDateTime created;
    private LocalDateTime updated;
}
