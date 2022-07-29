package com.example.social_network_fpt_be.controller;

import com.example.social_network_fpt_be.models.Follow;
import com.example.social_network_fpt_be.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Hashtable;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    @GetMapping(path = "")
    public ResponseEntity<List<Follow>> getAllFollow() {
        return ResponseEntity.status(HttpStatus.OK).body(followService.getAllFollow());
    }

    @GetMapping(path = "/{id_user_follow}")
    public ResponseEntity<List<Follow>> getFollowUser(@PathVariable Long id_user_follow) {
        return ResponseEntity.status(HttpStatus.OK).body(followService.getFollowUser(id_user_follow));
    }

    @PostMapping(path = "")
    public ResponseEntity<Follow> createFollow(@RequestBody Follow follow){
        return ResponseEntity.status(HttpStatus.OK).body(followService.createFollow(follow));
    }

    @DeleteMapping(path = "")
    public ResponseEntity<String> deleteFollow(@RequestBody Follow follow){
        String result = followService.deleteFollow(follow);
        if (result.equals("success")) {
            return ResponseEntity.status(HttpStatus.OK).body("Delete post success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete post fail");
        }
    }

//    @GetMapping(path= "/{id_user_follow}")
//    public ResponseEntity<List<Follow>> getListFriendUser(@PathVariable Long id_user_follow){
//        return ResponseEntity.status(HttpStatus.OK).body(followService.getListFriendUser(id_user_follow));
//    }
//
//    @GetMapping(path= "/")
//    public ResponseEntity<List<Follow>> getListFriend(){
//        return ResponseEntity.status(HttpStatus.OK).body(followService.getListFriend());
//    }

    @GetMapping(path= "/isFriend")
    public ResponseEntity<Boolean> isFriend(@RequestBody Follow follow){
        return ResponseEntity.status(HttpStatus.OK).body(followService.isFriend(follow));
    }
}
