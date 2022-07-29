package com.example.social_network_fpt_be.service;

import com.example.social_network_fpt_be.models.Follow;
import com.example.social_network_fpt_be.models.Image;
import com.example.social_network_fpt_be.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class FollowService {

    private final FollowRepository followRepository;

    @Autowired
    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public List<Follow> getAllFollow(){
        try {
            return followRepository.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Follow> getFollowUser(Long id_user_follow){
        try {
            return followRepository.getFollowUser(id_user_follow);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Follow createFollow(Follow follow){
        try {
            followRepository.save(follow);
            return follow;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String deleteFollow(Follow follow) {
        try{
            followRepository.deleteFollow(follow.getMyKey().getId_user_follow(), follow.getMyKey().getId_user_followed());
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    public List<Follow> getListFriend(){
        try{
            return followRepository.getListFriend();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Follow> getListFriendUser(Long id_user_follow){
        try{
            List<Follow> follow = followRepository.getListFriend();
            List<Follow> result = new ArrayList<>();
            for (Follow fl : follow){
                if (Objects.equals(fl.getMyKey().getId_user_follow(), id_user_follow)) {
                    result.add(fl);
                }
            }
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Boolean isFriend(Follow follow){
        try{
            List<Follow> getFollow = followRepository.getListFriend();
            for (Follow fl : getFollow){
                if (Objects.equals(fl.getMyKey().getId_user_follow(), follow.getMyKey().getId_user_follow()) && Objects.equals(fl.getMyKey().getId_user_followed(), follow.getMyKey().getId_user_followed())){
                    return true;
                }
            }
            return false;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
