package com.example.social_network_fpt_be.service;

import com.example.social_network_fpt_be.DTO.UserDto;
import com.example.social_network_fpt_be.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User saveUser(User user);

    List<User> getUsers();

    User getUserByUsername(String username);

    User getUserById(Long id);

    User updateUser(User user);

    List<User> searchByUsername(String name);
}
