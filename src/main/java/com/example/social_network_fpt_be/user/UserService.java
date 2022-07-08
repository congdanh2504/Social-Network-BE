package com.example.social_network_fpt_be.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    User saveUser(User user);

    Optional<User> getUser(Long id);

    List<User> getUsers();

    User getUserByUsername(String username);
}
