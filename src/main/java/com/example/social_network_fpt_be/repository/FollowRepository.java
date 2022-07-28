package com.example.social_network_fpt_be.repository;

import com.example.social_network_fpt_be.models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT f.id_user_follow, f.id_user_followed FROM Follow f WHERE f.id_user_follow = ?1")
    List<Follow> getFollowUser(Long id_user_follow);

    @Query("SELECT f FROM Follow f WHERE f.id_follow = ?1")
    Follow getFollowId(Long id_follow);

    @Query("DELETE FROM Follow f WHERE f.id_user_follow = ?1 and f.id_user_followed = ?2")
    void deleteFollow(Long id_user_follow, Long id_user_followed);

    @Query("SELECT f1.id_user_follow = f2.id_user_followed\n" +
            "FROM follow f1\n" +
            "INNER JOIN follow f2 \n" +
            "ON f1.id_user_follow = f2.id_user_followed and f1.id_user_followed = f2.id_user_follow\n")
    List<Follow> getListFriend();
}
