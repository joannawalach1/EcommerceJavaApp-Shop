package pl.com.coders.shop2.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    List<Order> order;
    private LocalDateTime created;
    private LocalDateTime updated;
}
