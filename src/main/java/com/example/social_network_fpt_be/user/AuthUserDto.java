package com.example.social_network_fpt_be.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class AuthUserDto {
    @NotBlank
    private String username;
    @Min(value = 6, message = "Password must be at least 6 characters")
    private String password;
    @Email(message = "Must be email")
    private String email;
}
