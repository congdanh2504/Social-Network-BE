package com.example.social_network_fpt_be.DTO;

import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

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
}
