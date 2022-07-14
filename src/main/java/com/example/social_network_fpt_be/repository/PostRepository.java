package com.example.social_network_fpt_be.repository;


import com.example.social_network_fpt_be.model.Image;
import com.example.social_network_fpt_be.model.Post;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(
            value = "SELECT p.id_post, p.id_user, p.title, p.description, p.create_date, p.post_video, v.url " +
                    "FROM Post p " +
                    "LEFT JOIN Video v " +
                    "ON p.post_video = v.id_video ",
            nativeQuery = true)
    List<Object> getAllPost();

    @Query(
            value = "SELECT p.id_post, p.id_user, p.title, p.description, p.create_date, p.post_video, v.url " +
                    "FROM Post p " +
                    "LEFT JOIN Video v " +
                    "ON p.post_video = v.id_video " +
                    "WHERE p.id_post = ?1",
            nativeQuery = true)
    Object getPostBy(int id);


}
