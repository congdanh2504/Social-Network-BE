package com.example.social_network_fpt_be.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditPostDto {
    List<String> newImages = new ArrayList<>();
    List<String> oldImages = new ArrayList<>();
    @NotBlank
    private String title;
    @NotBlank
    private String description;
}
