package com.example.social_network_fpt_be.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.social_network_fpt_be.models.Post;
import com.example.social_network_fpt_be.service.dtos.DetailUserDto;
import com.example.social_network_fpt_be.service.dtos.UploadPostDto;
import com.example.social_network_fpt_be.service.dtos.UpdateUserDto;
import com.example.social_network_fpt_be.models.User;
import com.example.social_network_fpt_be.service.dtos.UserDto;
import com.example.social_network_fpt_be.repository.UserRepository;
import com.example.social_network_fpt_be.util.ImageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Transactional
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final PostService postService;
    private final FollowService followService;
    private final LikeRecordService likeRecordService;
    private final Environment env;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ImageService imageService, PostService postService, FollowService followService, LikeRecordService likeRecordService, Environment env) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.postService = postService;
        this.followService = followService;
        this.likeRecordService = likeRecordService;
        this.env = env;
    }

    public boolean checkEmailAlreadyUsed(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean checkUsernameAlreadyUsed(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> result = new ArrayList<>();
        users.forEach((user) -> {
            UserDto userDto = UserDto.toUserDto(user);
            String avt = imageService.getAvatarByUser(user.getId());
            userDto.setAvt(avt);
            result.add(userDto);
        });
        return result;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserDto getProfile(String username) {
        User user = userRepository.findByUsername(username);
        UserDto userDto = UserDto.toUserDto(user);
        String avt = imageService.getAvatarByUser(user.getId());
        userDto.setAvt(avt);
        return userDto;
    }

    public DetailUserDto getDetailUser(String username) {
        DetailUserDto detailUserDto = new DetailUserDto();
        UserDto userDto = getProfile(username);
        detailUserDto.setId(userDto.getId());
        detailUserDto.setUsername(userDto.getUsername());
        detailUserDto.setFirstName(userDto.getFirstName());
        detailUserDto.setLastName(userDto.getLastName());
        detailUserDto.setEmail(userDto.getEmail());
        detailUserDto.setAvt(userDto.getAvt());
        String coverImage = imageService.getCoverImageByUser(userDto.getId());
        detailUserDto.setCover(coverImage);
        detailUserDto.setPhone(userDto.getPhone());
        detailUserDto.setDescription(userDto.getDescription());
        detailUserDto.setRole(userDto.getRole());
        detailUserDto.setFriends(followService.getFriends(userDto.getId()));
        detailUserDto.setPosts(postService.getUserPosts(userDto.getId()));
        List<String> images = imageService.getPostImageByUser(userDto.getId());
        detailUserDto.setFollowers(followService.getFollowers(userDto.getId()));
        detailUserDto.setFollowings(followService.getFollowings(userDto.getId()));
        if (!userDto.getAvt().equals("")) images.add(userDto.getAvt());
        if (!coverImage.equals("")) images.add(coverImage);
        detailUserDto.setImages(images);
        return detailUserDto;
    }

    public UserDto getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        UserDto userDto = UserDto.toUserDto(user.get());
        String avt = imageService.getAvatarByUser(id);
        userDto.setAvt(avt);
        return userDto;
    }

    public UserDto updateUser(UpdateUserDto updateUser, String username) throws IOException {
        User currentUser = getUserByUsername(username);
        if (updateUser.getAvtImage() != null) {
            imageService.createImage(updateUser.getAvtImage(), ImageType.USER_AVT.toString(), currentUser.getId());
        }
        if (updateUser.getCoverImage() != null) {
            imageService.createImage(updateUser.getCoverImage(), ImageType.USER_COVER.toString(), currentUser.getId());
        }
        currentUser.setFirstName(updateUser.getFirstName());
        currentUser.setLastName(updateUser.getLastName());
        currentUser.setPhone(updateUser.getPhone());
        currentUser.setDescription(updateUser.getDescription());
        UserDto userDto = UserDto.toUserDto(userRepository.save(currentUser));
        String avt = imageService.getAvatarByUser(currentUser.getId());
        userDto.setAvt(avt);
        return userDto;
    }

    public Post createPost(UploadPostDto postDto, String username) throws IOException {
        User user = getUserByUsername(username);
        return postService.createPost(postDto, user.getId());
    }

    public List<UserDto> searchByUsername(String name) {
        String[] words = name.split(" ");
        Set<User> distinctUsers = new HashSet<>();
        Arrays.stream(words).forEach((word) -> {
            distinctUsers.addAll(userRepository.searchByUsername(removeAccent(word)));
        });
        List<UserDto> result = new ArrayList<>();
        distinctUsers.forEach((user) -> {
            UserDto userDto = UserDto.toUserDto(user);
            String avt = imageService.getAvatarByUser(user.getId());
            userDto.setAvt(avt);
            result.add(userDto);
        });
        return result;
    }

    public void likePost(Long post_id, String username) {
        User user = getUserByUsername(username);
        likeRecordService.likePost(post_id, user.getId());
    }

    public void unlikePost(Long post_id, String username) {
        User user = getUserByUsername(username);
        likeRecordService.unlikePost(post_id, user.getId());
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
