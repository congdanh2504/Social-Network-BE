package com.example.social_network_fpt_be.repository;

import com.example.social_network_fpt_be.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.id <> :user_id AND u.id NOT IN " +
            "(SELECT f.myKey.id_user_followed FROM Follow f " +
            "WHERE f.myKey.id_user_follow = :user_id)")
    List<User> getUnfollowUser(Long user_id);

    @Query("SELECT u FROM User u WHERE function('dbo.ufn_removeMark', u.firstName) LIKE %:name% OR function('dbo.ufn_removeMark', u.lastName) LIKE %:name%")
    List<User> searchByUsername(String name);

}
