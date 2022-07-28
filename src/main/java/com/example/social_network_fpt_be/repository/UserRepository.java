package com.example.social_network_fpt_be.repository;

import com.example.social_network_fpt_be.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE function('dbo.ufn_removeMark', u.firstName) LIKE %:name% " +
            "OR function('dbo.ufn_removeMark', u.lastName) LIKE %:name% " +
            "OR u.username LIKE %:name%")
    List<User> searchByUsername(String name);
}
