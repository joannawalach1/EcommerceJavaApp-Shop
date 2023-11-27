package pl.com.coders.shop2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.coders.shop2.domain.Users;
import pl.com.coders.shop2.domain.dto.UserDto;
import pl.com.coders.shop2.mapper.UserMapper;
import pl.com.coders.shop2.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto create(UserDto userDto) {
        Users user = userMapper.dtoToUser(userDto);
        Users savedUser = userRepository.create(user);
        return userMapper.userToDto(savedUser);
    }

    public UserDto findByEmail(String email) {
        return userMapper.userToDto(userRepository.findByEmail(email));
    }
}
