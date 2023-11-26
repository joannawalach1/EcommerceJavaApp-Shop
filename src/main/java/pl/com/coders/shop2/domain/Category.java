package pl.com.coders.shop2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @EqualsAndHashCode
    @ToString
    @Table(name = "CATEGORY")
    public class Category {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @OneToMany(mappedBy = "category", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
        @JsonIgnore
        private Set<Product> products;
        private String name;
        @CreationTimestamp
        private LocalDateTime created;
        @UpdateTimestamp
        private LocalDateTime updated;

        @Override
        public String toString() {
            return "Category{id=" + id + ", name='" + name + ", created=" + created + ", updated=" + updated + "}";
        }
    }