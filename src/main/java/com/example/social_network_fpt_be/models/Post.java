package com.example.social_network_fpt_be.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_post;
    private Long id_user;
    private String title;
    private String description;
    private LocalDateTime create_date;
    // create a field is not a column in database
    @OneToMany(
            mappedBy = "id_post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Video> videos = new ArrayList<>();
    @Transient
    private String url;
}
