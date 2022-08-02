package com.example.social_network_fpt_be.repository;


import com.example.social_network_fpt_be.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p ORDER BY p.create_date DESC ")
    List<Post> getAllPost();

    @Query(
            value = "SELECT p.id_post, p.id_user, p.title, p.description, p.create_date, p.post_video, v.url " +
                    "FROM Post p " +
                    "LEFT JOIN Video v " +
                    "ON p.post_video = v.id_video " +
                    "WHERE p.id_post = ?1",
            nativeQuery = true)
    Object getPostBy(Long id);

    @Query(
            value = "SELECT TOP 5 * " +
                    "FROM Post p " +
                    "ORDER BY p.create_date desc",
            nativeQuery = true)
    List<Post> getFiveLatestPosts();

    @Query("SELECT p FROM Post p WHERE p.id_user = :user_id ORDER BY p.create_date DESC ")
    List<Post> getUserPosts(Long user_id);

    @Query("SELECT p " +
            "FROM Post p " +
            "WHERE p.id_user = :user_id OR " +
            "p.id_user IN " +
            "(SELECT f.myKey.id_user_followed FROM Follow f " +
            "WHERE f.myKey.id_user_follow = :user_id) " +
            "ORDER BY p.create_date desc ")
    List<Post> getFollowingPosts(Long user_id);
}
