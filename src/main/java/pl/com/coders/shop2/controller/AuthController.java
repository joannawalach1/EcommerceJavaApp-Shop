package pl.com.coders.shop2.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.coders.shop2.domain.User;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping
    public User auth() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return new User(login);
    }
}
