package pl.com.coders.shop2.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    @OneToMany(mappedBy = "user")
    private Set<Order> orders;
    private LocalDateTime created;
    private LocalDateTime updated;

    public User(long id, String mail, String first_name, String last_name, String password) {
    }
}
