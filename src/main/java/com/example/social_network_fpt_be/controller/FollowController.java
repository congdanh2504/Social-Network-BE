package com.example.social_network_fpt_be.controller;

import com.example.social_network_fpt_be.models.Follow;
import com.example.social_network_fpt_be.service.FollowService;
import com.example.social_network_fpt_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Hashtable;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @GetMapping(path = "")
    public ResponseEntity<List<Follow>> getAllFollow() {
        return ResponseEntity.status(HttpStatus.OK).body(followService.getAllFollow());
    }

//    @GetMapping(path = "/{id_user_follow}")
//    public ResponseEntity<List<Follow>> getFollowUser(@PathVariable Long id_user_follow) {
//        return ResponseEntity.status(HttpStatus.OK).body(followService.getFollowUser(id_user_follow));
//    }

    @PostMapping(path = "")
    public ResponseEntity<Follow> createFollow(@RequestParam("id_user_followed") Long id_user_followed,
                                               Authentication authentication){
        Long id_user_follow = userService.getUserByUsername(authentication.getName()).getId();
        return ResponseEntity.status(HttpStatus.OK).body(followService.createFollow(id_user_follow, id_user_followed));
    }

    @DeleteMapping(path = "")
    public ResponseEntity<String> deleteFollow(@RequestParam("id_user_followed") Long id_user_followed,
                                               Authentication authentication){
        Long id_user_follow = userService.getUserByUsername(authentication.getName()).getId();
        String result = followService.deleteFollow(id_user_follow, id_user_followed);
        if (result.equals("success")) {
            return ResponseEntity.status(HttpStatus.OK).body("Delete follow success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete follow fail");
        }
    }

//    @GetMapping(path= "/listFriend")
//    public ResponseEntity<List<Hashtable<String, Object>>> getListFriendUser(Authentication authentication){
//        Long id_user_follow = userService.getUserByUsername(authentication.getName()).getId();
//        return ResponseEntity.status(HttpStatus.OK).body(followService.getListFriendUser(id_user_follow));
//    }
//
//    @GetMapping(path= "/")
//    public ResponseEntity<List<Follow>> getListFriend(){
//        return ResponseEntity.status(HttpStatus.OK).body(followService.getListFriend());
//    }

//    @GetMapping(path= "/isFriend")
//    public ResponseEntity<Boolean> isFriend(@RequestParam("id_user_followed") Long id_user_followed,
//                                            Authentication authentication){
//        Long id_user_follow = userService.getUserByUsername(authentication.getName()).getId();
//        return ResponseEntity.status(HttpStatus.OK).body(followService.isFriend(id_user_follow,id_user_followed ));
//    }
}
