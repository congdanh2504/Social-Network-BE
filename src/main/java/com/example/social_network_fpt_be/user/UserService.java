package com.example.social_network_fpt_be.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    UserDto saveUser(User user);

    List<UserDto> getUsers();

    UserDto getUserByUsername(String username);

    UserDto getUserById(Long id);
}
