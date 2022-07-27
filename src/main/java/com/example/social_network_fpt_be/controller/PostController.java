package com.example.social_network_fpt_be.controller;

import com.example.social_network_fpt_be.service.ImageService;
import com.example.social_network_fpt_be.service.PostService;
import com.example.social_network_fpt_be.service.UserService;
import com.example.social_network_fpt_be.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/post")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;

    @GetMapping(path = "")
    public ResponseEntity<List<Hashtable<String, Object>>> getPostList() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostList());
    }

    @GetMapping(path = "{id_post}")
    public ResponseEntity<Hashtable<String, Object>> getPostById(@PathVariable Long id_post) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostByID(id_post));
    }

    @PostMapping(path = "", consumes = "multipart/form-data")
    public ResponseEntity<Object> createPost(@RequestPart("post_image") List<MultipartFile> post_image,
                                             Authentication authentication,
                                             @RequestParam("title") String title,
                                             @RequestParam("description") String description) throws IOException {
        Long id_user = userService.getUserByUsername(authentication.getName()).getId();
//        if (post_video.getContentType() != null) {
//            Hashtable<String, Object> checkVideo = videoService.checkFile(post_video);
//            if (checkVideo.get("status").equals(0)) {
//                if (checkVideo.get("message").equals("Video file is empty")) {
//                } else {
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(checkVideo.get("message"));
//                }
//            }
//        }
        if (post_image.get(0).getContentType() != null) {
            for (MultipartFile multipartFile : post_image) {
                Hashtable<String, Object> checkImage = imageService.checkFile(multipartFile);
                if (checkImage.get("status").equals(0)) {
                    if (checkImage.get("message").equals("Image file is empty")) {
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(checkImage.get("message"));
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(postService.createPost(post_image, id_user, title, description));
    }

    @PutMapping(path = "{id_post}")
    public ResponseEntity<Object> updatePost(@PathVariable Long id_post,
                                             @RequestPart("post_video") MultipartFile post_video,
                                             @RequestPart("post_image") List<MultipartFile> post_image,
                                             Authentication authentication,
                                             @RequestParam("title") String title,
                                             @RequestParam("description") String description) throws IOException {
        Long id_user = userService.getUserByUsername(authentication.getName()).getId();
        if (post_video.getContentType() != null) {
            Hashtable<String, Object> checkVideo = videoService.checkFile(post_video);
            if (checkVideo.get("status").equals(0)) {
                if (checkVideo.get("message").equals("Video file is empty")) {
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(checkVideo.get("message"));
                }
            }
        }
        if (post_image.get(0).getContentType() != null) {
            for (MultipartFile multipartFile : post_image) {
                Hashtable<String, Object> checkImage = imageService.checkFile(multipartFile);
                if (checkImage.get("status").equals(0)) {
                    if (checkImage.get("message").equals("Image file is empty")) {
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(checkImage.get("message"));
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(id_post, post_video, post_image, id_user, title, description));
    }

    @DeleteMapping(path = "{id_post}")
    public ResponseEntity<Object> deletePost(@PathVariable Long id_post) {
        String result = postService.deletePost(id_post);
        if (result.equals("success")) {
            return ResponseEntity.status(HttpStatus.OK).body("Delete post success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete post fail");
        }
    }

}
