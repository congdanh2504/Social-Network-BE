package com.example.social_network_fpt_be.repository;

import com.example.social_network_fpt_be.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM Comment c WHERE c.id_comment_father = ?1")
    List<Comment> getCommentByFather(Integer id);

    @Query("SELECT c FROM Comment c WHERE c.id_comment_father is null")
    List<Comment> getCommentRoot();

}