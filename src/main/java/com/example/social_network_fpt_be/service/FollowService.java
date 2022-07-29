package com.example.social_network_fpt_be.service;

import ch.qos.logback.classic.pattern.SyslogStartConverter;
import com.example.social_network_fpt_be.models.Follow;
import com.example.social_network_fpt_be.models.Image;
import com.example.social_network_fpt_be.models.MyKey;
import com.example.social_network_fpt_be.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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

//    public List<Follow> getFollowUser(Long id_user_follow){
//        try {
//            return followRepository.getFollowUser(id_user_follow);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return null;
//        }
//    }

    public Follow createFollow(Long id_user_follow, Long id_user_followed){
        try {
            Follow follow = new Follow(new MyKey(id_user_follow, id_user_followed));
            followRepository.save(follow);
            return follow;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String deleteFollow(Long id_user_follow, Long id_user_followed) {
        try{
            followRepository.deleteFollow(id_user_follow, id_user_followed);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    public List<Hashtable<String, Object>> getListFriendUser(Long id_user_follow){
        try{
            List<Object> follow = followRepository.getListFriend();
            List<Hashtable<String, Object>> friends = new ArrayList<>();
            for (Object fl : follow){
                if (Objects.equals(((Object[]) fl)[0], id_user_follow)) {
                    Hashtable<String,Object> friend = new Hashtable<>();
                    friend.put("id_user_follow", ((Object[]) fl)[0]);
                    friend.put("id_user_followed", ((Object[]) fl)[1]);
                    friends.add(friend);
                }
            }
            return friends;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Boolean isFriend(Long id_user_follow, Long id_user_followed){
        try{
            List<Object> getFriend = followRepository.getListFriend();
            for (Object fl : getFriend){
                if (Objects.equals(((Object[]) fl)[0], id_user_follow) && Objects.equals(((Object[]) fl)[0], id_user_followed)){
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
