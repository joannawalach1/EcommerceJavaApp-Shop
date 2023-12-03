package pl.com.coders.shop2.domain.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
