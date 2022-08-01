package com.example.social_network_fpt_be.service.dtos;

import com.example.social_network_fpt_be.models.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailPostDto {
    private Long id;
    private String title;
    private UserDto user;
    private String description;
    private LocalDateTime create_date;
    private List<UserDto> likeUsers = new ArrayList<>();
    private List<CommentDto> comments = new ArrayList<>();
    private List<Image> images = new ArrayList<>();
}
