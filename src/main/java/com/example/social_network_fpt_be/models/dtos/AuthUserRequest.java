package com.example.social_network_fpt_be.models.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class AuthUserRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Min(value = 6, message = "Password must be at least 6 characters")
    private String password;
    @Email(message = "Must be email")
    private String email;
}
