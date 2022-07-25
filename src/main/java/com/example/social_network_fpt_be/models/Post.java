package com.example.social_network_fpt_be.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_post;
    private Integer id_user;
    private String title;
    private String description;
    private LocalDateTime create_date;
    private Integer post_video;
    // create a field is not a column in database
    @Transient
    private String url;

    public Post(){}

    public Post(Integer id_post, Integer id_user, String title, String description, LocalDateTime create_date, Integer post_video, String url) {
        this.id_post = id_post;
        this.id_user = id_user;
        this.title = title;
        this.description = description;
        this.create_date = create_date;
        this.post_video = post_video;
        this.url = url;
    }

    public Integer getId_post() {
        return id_post;
    }

    public void setId_post(Integer id_post) {
        this.id_post = id_post;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreate_date() {
        return create_date;
    }

    public void setCreate_date(LocalDateTime create_date) {
        this.create_date = create_date;
    }

    public Integer getPost_video() {
        return post_video;
    }

    public void setPost_video(Integer post_video) {
        this.post_video = post_video;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



//    @Override
//    public String toString() {
//        return "Post{" +
//                "id_post=" + id_post +
//                ", id_used=" + id_used +
//                ", title='" + title + '\'' +
//                ", description='" + description + '\'' +
//                ", create_date=" + create_date +
//                ", post_video=" + post_video +
//                '}';
//    }
}
