package com.example.social_network_fpt_be.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.Hashtable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailUserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String phone;
    private int role;
    private int isBlock;
    private String description;
    private String avt = "";
    private List<UserDto> friends;
    private List<DetailPostDto> posts;
}
