package com.example.social_network_fpt_be.models.controller;

import com.example.social_network_fpt_be.service.CommentService;
import com.example.social_network_fpt_be.service.ImageService;
import com.example.social_network_fpt_be.service.UserService;
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
@RequestMapping(path = "/api/v1/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;

    @GetMapping(path = "")
    public ResponseEntity<List<Hashtable<String, Object>>> getCommentList() {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllComments());
    }

    @GetMapping(path = "/{id_comment}")
    public ResponseEntity<Hashtable<String, Object>> getCommentById(@PathVariable Long id_comment) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentById(id_comment));
    }

    @PostMapping(path = "")
    public ResponseEntity<Object> createComment(@RequestPart("comment_image") MultipartFile comment_image,
                                                @RequestParam("id_post") Long id_post,
                                                Authentication authentication,
                                                @RequestParam("id_comment_father") Long id_comment_father,
                                                @RequestParam("comment") String comment) throws IOException {
        Long id_user_comment = userService.getUserByUsername(authentication.getName()).getId();
        if (comment_image.getContentType() != null) {
            Hashtable<String, Object> checkImage = imageService.checkFile(comment_image);
            if (checkImage.get("status").equals(0)){
                if (checkImage.get("message").equals("Image file is empty")){

                }else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(checkImage.get("message"));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(commentService.createComment(comment_image, id_post, id_user_comment, id_comment_father, comment));
    }

    @PutMapping(path = "{id_comment}")
    public ResponseEntity<Object> updateComment(@PathVariable("id_comment") Long id_comment,
                                                @RequestPart("comment_image") MultipartFile comment_image,
                                                @RequestParam("comment") String comment,
                                                @RequestParam("like_number") Long like_number) throws IOException {
        if (comment_image.getContentType() != null) {
            Hashtable<String, Object> checkImage = imageService.checkFile(comment_image);
            if (checkImage.get("status").equals(0)){
                if (checkImage.get("message").equals("Image file is empty")){

                }else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(checkImage.get("message"));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(id_comment, comment_image,  comment, like_number));
    }

    @DeleteMapping(path = "/{id_comment}")
    public ResponseEntity<Object> deleteComment(@PathVariable("id_comment") Long id_comment) {
        String result = commentService.deleteComment(id_comment);
        if (result.equals("success")) {
            return ResponseEntity.status(HttpStatus.OK).body("Delete post success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete post fail");
        }
    }
}
