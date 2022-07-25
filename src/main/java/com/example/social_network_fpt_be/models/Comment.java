package com.example.social_network_fpt_be.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_comment;
    private Long id_post;
    private Long id_user_comment;
    private Long id_comment_father;
    private String comment;
    private Long like_number;
    private LocalDateTime create_date;

    public Comment() {
    }

    public Comment(Long id_post, Long id_user_comment, Long id_comment_father, String comment, Long like_number, LocalDateTime create_date) {
        this.id_post = id_post;
        this.id_user_comment = id_user_comment;
        this.id_comment_father = id_comment_father;
        this.comment = comment;
        this.like_number = like_number;
        this.create_date = create_date;
    }

    public Long getId_comment() {
        return id_comment;
    }

    public void setId_comment(Long id_comment) {
        this.id_comment = id_comment;
    }

    public Long getId_post() {
        return id_post;
    }

    public void setId_post(Long id_post) {
        this.id_post = id_post;
    }

    public Long getId_user_comment() {
        return id_user_comment;
    }

    public void setId_user_comment(Long id_user_comment) {
        this.id_user_comment = id_user_comment;
    }

    public Long getId_comment_father() {
        return id_comment_father;
    }

    public void setId_comment_father(Long id_comment_father) {
        this.id_comment_father = id_comment_father;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getLike_number() {
        return like_number;
    }

    public void setLike_number(Long like_number) {
        this.like_number = like_number;
    }

    public LocalDateTime getCreate_date() {
        return create_date;
    }

    public void setCreate_date(LocalDateTime create_date) {
        this.create_date = create_date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id_comment=" + id_comment +
                ", id_post=" + id_post +
                ", id_user_comment=" + id_user_comment +
                ", id_comment_father=" + id_comment_father +
                ", comment='" + comment + '\'' +
                ", like_number=" + like_number +
                ", create_date=" + create_date +
                '}';
    }

}
