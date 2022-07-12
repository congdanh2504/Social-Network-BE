package com.example.social_network_fpt_be.DTO;

import com.example.social_network_fpt_be.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Null;

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

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getPhone(), user.getRole(), user.getIsBlock(), user.getDescription());
    }
}
