package pl.com.coders.shop2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.UserDto;
import pl.com.coders.shop2.mapper.UserMapper;
import pl.com.coders.shop2.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto create(UserDto userDto) {
        User user = userMapper.dtoToUser(userDto);
        User savedUser = userRepository.create(user);
        savedUser.setCreated(LocalDateTime.now());
        savedUser.setUpdated(LocalDateTime.now());
        return userMapper.userToDto(savedUser);
    }

    public UserDto findByEmail(String email) {
        User byEmail = userRepository.findByEmail(email);
        return userMapper.userToDto(byEmail);
    }


}
