package com.example.social_network_fpt_be.controller;

import com.example.social_network_fpt_be.models.Post;
import com.example.social_network_fpt_be.service.dtos.*;
import com.example.social_network_fpt_be.models.User;
import com.example.social_network_fpt_be.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("profile")
    public ResponseEntity<UserDto> getUserById(Authentication authentication) {
        return ResponseEntity.ok().body(userService.getProfile(authentication.getName()));
    }

    @GetMapping("detail-user/{username}")
    public ResponseEntity<DetailUserDto> getDetailProfile(@PathVariable String username) {
        return ResponseEntity.ok().body(userService.getDetailUser(username));
    }

    @GetMapping("search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String username) {
        return ResponseEntity.ok().body(userService.searchByUsername(username));
    }

    @GetMapping("unfollow-users")
    public ResponseEntity<List<UserDto>> getUnFollowUsers(Authentication authentication) {
        return ResponseEntity.ok(userService.getUnFollowUsers(authentication.getName()));
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @PutMapping(path = "", consumes = "multipart/form-data")
    public ResponseEntity<UserDto> updateUser(@Valid @ModelAttribute UpdateUserDto updateUser, Authentication authentication) throws IOException {
        return ResponseEntity.ok().body(userService.updateUser(updateUser, authentication.getName()));
    }

    @PostMapping(path = "/post")
    public ResponseEntity<Post> createPost(@Valid @RequestBody UploadPostDto postDto, Authentication authentication) throws IOException {
        return ResponseEntity.ok().body(userService.createPost(postDto, authentication.getName()));
    }

    @PutMapping(path = "/change-avt", consumes = "multipart/form-data")
    public ResponseEntity<?> changeAvt(@RequestPart("avt") MultipartFile avt, Authentication authentication) {
        userService.changeAvt(avt, authentication.getName());
        return ResponseEntity.ok(true);
    }

    @GetMapping("")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthUserDto user) {
        if (userService.checkEmailAlreadyUsed(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already used");
        }
        if (userService.checkUsernameAlreadyUsed(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username already used");
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users").toUriString());
        return ResponseEntity.created(uri).body(UserDto.toUserDto(userService.saveUser(newUser)));
    }

    @PostMapping("like-post/{post_id}")
    public void likePost(@PathVariable Long post_id, Authentication authentication) {
        userService.likePost(post_id, authentication.getName());
    }

    @DeleteMapping("unlike-post/{post_id}")
    public void unlikePost(@PathVariable Long post_id, Authentication authentication) {
        userService.unlikePost(post_id, authentication.getName());
    }

    @GetMapping("refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                String [] tokens = userService.refreshToken(refreshToken);
                Map<String, String> token = new HashMap<>();
                token.put("access_token", tokens[0]);
                token.put("refresh_token", tokens[1]);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), token);
            } catch (Exception e) {
                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}