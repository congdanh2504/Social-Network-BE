package com.example.social_network_fpt_be.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "role_user")
    private int role;

    @Column(name = "is_block")
    private int isBlock;

    @Column(name = "description")
    private String description;

    public User() {
    }

    public User(String email, String firstName) {
        this.email = email;
        this.firstName = firstName;
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }
}
