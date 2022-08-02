package com.example.social_network_fpt_be.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private UserDto user;
    private List<CommentDto> children = new ArrayList<>();
    private String text;
    private LocalDateTime create_date;
}
