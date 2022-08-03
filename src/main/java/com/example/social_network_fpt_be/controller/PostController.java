package com.example.social_network_fpt_be.controller;

import com.example.social_network_fpt_be.models.Post;
import com.example.social_network_fpt_be.service.*;
import com.example.social_network_fpt_be.service.dtos.DetailPostDto;
import com.example.social_network_fpt_be.service.dtos.EditPostDto;
import com.example.social_network_fpt_be.service.dtos.UploadPostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/post")
public class PostController {

    private final PostService postService;
    private final ImageService imageService;
    private final VideoService videoService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, ImageService imageService, VideoService videoService, UserService userService) {
        this.postService = postService;
        this.imageService = imageService;
        this.videoService = videoService;
        this.userService = userService;
    }

    @GetMapping(path = "")
    public ResponseEntity<List<DetailPostDto>> getPostList() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostList());
    }

    @GetMapping(path = "following")
    public ResponseEntity<List<DetailPostDto>> getFollowingPosts(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getFollowingPosts(authentication.getName()));
    }

    @GetMapping(path = "/{post_id}")
    public ResponseEntity<DetailPostDto> getPostById(@PathVariable Long post_id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(post_id));
    }

//    @GetMapping(path = "{id_post}")
//    public ResponseEntity<Hashtable<String, Object>> getPostById(@PathVariable Long id_post) {
//        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostByID(id_post));
//    }

    @PutMapping(path = "/{post_id}")
    public void createPost(@Valid @RequestBody EditPostDto postDto, @PathVariable Long post_id) throws IOException {
//        System.out.println(postDto);
//        System.out.println(post_id);
        postService.editPost(postDto, post_id);
    }

    @GetMapping(path = "five-latest-posts")
    public ResponseEntity<List<DetailPostDto>> getFiveLatestPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getFiveLatestPosts());
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

//    @PutMapping(path = "{id_post}")
//    public ResponseEntity<Object> updatePost(@PathVariable Long id_post,
//                                             @RequestPart("post_video") MultipartFile post_video,
//                                             @RequestPart("post_image") List<MultipartFile> post_image,
//                                             Authentication authentication,
//                                             @RequestParam("title") String title,
//                                             @RequestParam("description") String description) throws IOException {
//        Long id_user = userService.getUserByUsername(authentication.getName()).getId();
//        if (post_video.getContentType() != null) {
//            Hashtable<String, Object> checkVideo = videoService.checkFile(post_video);
//            if (checkVideo.get("status").equals(0)) {
//                if (checkVideo.get("message").equals("Video file is empty")) {
//                } else {
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(checkVideo.get("message"));
//                }
//            }
//        }
//        if (post_image.get(0).getContentType() != null) {
//            for (MultipartFile multipartFile : post_image) {
//                Hashtable<String, Object> checkImage = imageService.checkFile(multipartFile);
//                if (checkImage.get("status").equals(0)) {
//                    if (checkImage.get("message").equals("Image file is empty")) {
//                    } else {
//                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(checkImage.get("message"));
//                    }
//                }
//            }
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(id_post, post_video, post_image, id_user, title, description));
//    }

    @DeleteMapping(path = "{id_post}")
    public ResponseEntity<?> deletePost(@PathVariable Long id_post) {
        postService.deletePost(id_post);
        return ResponseEntity.ok(true);
    }

}
