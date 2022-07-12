package com.example.social_network_fpt_be.service;

import com.example.social_network_fpt_be.DTO.UserDto;
import com.example.social_network_fpt_be.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDto saveUser(User user);

    List<UserDto> getUsers();

    UserDto getUserByUsername(String username);

    UserDto getUserById(Long id);
}
