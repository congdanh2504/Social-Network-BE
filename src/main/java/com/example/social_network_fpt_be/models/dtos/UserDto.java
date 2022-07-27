package com.example.social_network_fpt_be.models.dtos;

import com.example.social_network_fpt_be.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String email;

    private String firstName;

    private String lastname;

    private String username;

    private String phone;

    private int role;

    private int isBlock;

    private String description;

    private String avt = "";

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getPhone(),
                user.getRole(),
                user.getIsBlock(),
                user.getDescription(),
                "");
    }
}
