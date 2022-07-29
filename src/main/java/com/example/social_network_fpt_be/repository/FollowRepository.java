package com.example.social_network_fpt_be.repository;

import com.example.social_network_fpt_be.models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT f.myKey.id_user_follow, f.myKey.id_user_followed FROM Follow f WHERE f.myKey.id_user_follow = ?1")
    List<Follow> getFollowUser(Long id_user_follow);

    @Modifying
    @Query("DELETE FROM Follow f WHERE f.myKey.id_user_follow = ?1 and f.myKey.id_user_followed = ?2")
    void deleteFollow(Long id_user_follow, Long id_user_followed);

    @Query("SELECT f1.myKey.id_user_follow, f1.myKey.id_user_followed " +
            "FROM Follow f1 " +
            "INNER JOIN Follow f2 " +
            "ON f1.myKey.id_user_follow = f2.myKey.id_user_followed and f1.myKey.id_user_followed = f2.myKey.id_user_follow")
    List<Object> getListFriend();
}
