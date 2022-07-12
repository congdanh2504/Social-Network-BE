package com.example.social_network_fpt_be.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.social_network_fpt_be.DTO.AuthUserDto;
import com.example.social_network_fpt_be.DTO.UpdateUserDto;
import com.example.social_network_fpt_be.DTO.UserDto;
import com.example.social_network_fpt_be.model.User;
import com.example.social_network_fpt_be.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final Environment env;

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(UserDto.toUserDto(userService.getUserById(id)));
    }

    @PutMapping()
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserDto user, Authentication authentication) {
        User currentUser = userService.getUserByUsername(authentication.getName());
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setPhone(user.getPhone());
        currentUser.setDescription(user.getDescription());
        return ResponseEntity.ok().body(UserDto.toUserDto(userService.updateUser(currentUser)));
    }

    @GetMapping("")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers().stream().map(UserDto::toUserDto).collect(Collectors.toList()));
    }

    @PostMapping("register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody AuthUserDto user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users").toUriString());
        return ResponseEntity.created(uri).body(UserDto.toUserDto(userService.saveUser(newUser)));
    }

    @GetMapping("refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String secret = env.getProperty("secret");
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userService.getUserByUsername(username);
                final int timeMillisInTenMinutes = 1000 * 10 * 60;
                String[] roles = {String.valueOf(user.getRole())};
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + timeMillisInTenMinutes))
                        .withIssuer(request.getRequestURI())
                        .withClaim("role", Arrays.stream(roles).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refresh_token);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }

        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}