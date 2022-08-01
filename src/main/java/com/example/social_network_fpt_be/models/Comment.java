package com.example.social_network_fpt_be.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_comment;
    private Long id_post;
    private Long id_user_comment;
    private Long id_comment_father;
    private String comment;
    private LocalDateTime create_date;
}
