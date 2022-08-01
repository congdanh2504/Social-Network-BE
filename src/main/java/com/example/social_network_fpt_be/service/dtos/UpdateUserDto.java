package com.example.social_network_fpt_be.service.dtos;

import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserDto {
    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
    @NotBlank
    @NumberFormat
    String phone;
    @NotBlank
    String description;
    MultipartFile avtImage;
    MultipartFile coverImage;
}
