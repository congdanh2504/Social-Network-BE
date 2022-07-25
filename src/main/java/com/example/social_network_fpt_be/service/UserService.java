package com.example.social_network_fpt_be.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.social_network_fpt_be.models.dtos.UpdateUserDto;
import com.example.social_network_fpt_be.models.User;
import com.example.social_network_fpt_be.repository.UserRepository;
import com.example.social_network_fpt_be.util.Constraints;
import com.google.api.client.util.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final Environment env;

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.get();
    }

    public User updateUser(UpdateUserDto updateUser, String username) throws IOException {
        User currentUser = getUserByUsername(username);
        if (updateUser.getAvtImage() != null) {
            imageService.createImage(updateUser.getAvtImage(), Constraints.USER_AVT, currentUser.getId().intValue());
        }
        if (updateUser.getCoverImage() != null) {
            imageService.createImage(updateUser.getCoverImage(), Constraints.USER_COVER, currentUser.getId().intValue());
        }
        currentUser.setFirstName(updateUser.getFirstName());
        currentUser.setLastName(updateUser.getLastName());
        currentUser.setPhone(updateUser.getPhone());
        currentUser.setDescription(updateUser.getDescription());
        return userRepository.save(currentUser);
    }

    public List<User> searchByUsername(String name) {
        String[] words = name.split(" ");
        Set<User> distinctUsers = new HashSet<>();
        Arrays.stream(words).forEach((word) -> {
            distinctUsers.addAll(userRepository.searchByUsername(removeAccent(word)));
        });
        return Lists.newArrayList(distinctUsers);
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }

    public String[] refreshToken(String refreshToken) {
        String secret = env.getProperty("secret");
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        String username = decodedJWT.getSubject();
        User user = getUserByUsername(username);
        final int timeMillisInOneDay = 1000 * 60 * 60 * 24;
        String[] roles = {String.valueOf(user.getRole())};
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + timeMillisInOneDay))
                .withIssuer("/api/v1/users/refresh-token")
                .withClaim("role", Arrays.stream(roles).collect(Collectors.toList()))
                .sign(algorithm);
        return new String[]{accessToken, refreshToken};
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole() + ""));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
