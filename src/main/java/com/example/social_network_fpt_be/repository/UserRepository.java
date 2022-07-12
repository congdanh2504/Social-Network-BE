package com.example.social_network_fpt_be.repository;

import com.example.social_network_fpt_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
