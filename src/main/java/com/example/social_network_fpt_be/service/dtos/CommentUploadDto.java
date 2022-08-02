package com.example.social_network_fpt_be.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentUploadDto {
    private Long id_post;
    private Long id_father_comment;
    @NotBlank
    private String text;
}
