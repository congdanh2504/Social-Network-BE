package com.example.social_network_fpt_be.models.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostDto {
    List<MultipartFile> post_image = new ArrayList<>();
    @NotBlank
    private String title;
    @NotBlank
    private String description;
}
