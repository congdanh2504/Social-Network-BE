package com.example.social_network_fpt_be.service;

import com.example.social_network_fpt_be.models.Follow;
import com.example.social_network_fpt_be.models.FollowKey;
import com.example.social_network_fpt_be.repository.FollowRepository;
import com.example.social_network_fpt_be.service.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    @Autowired
    public FollowService(FollowRepository followRepository, @Lazy UserService userService) {
        this.followRepository = followRepository;
        this.userService = userService;
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
            Follow follow = new Follow(new FollowKey(id_user_follow, id_user_followed));
            followRepository.save(follow);
            return follow;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Transactional
    public String deleteFollow(Long id_user_follow, Long id_user_followed) {
        try{
            followRepository.deleteFollow(id_user_follow, id_user_followed);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    public List<UserDto> getFriends(Long user_id) {
        List<Follow> followEachOtherList = followRepository.getFriends(user_id);
        List<UserDto> result = new ArrayList<>();
        for (Follow follow: followEachOtherList) {
            result.add(userService.getUserById(follow.getMyKey().getId_user_followed()));
        }
        return result;
    }

    public List<UserDto> getFollowers(Long user_id) {
        List<Follow> followEachOtherList = followRepository.getFollowers(user_id);
        List<UserDto> result = new ArrayList<>();
        for (Follow follow: followEachOtherList) {
            result.add(userService.getUserById(follow.getMyKey().getId_user_follow()));
        }
        return result;
    }

    public List<UserDto> getFollowings(Long user_id) {
        List<Follow> followEachOtherList = followRepository.getFollowings(user_id);
        List<UserDto> result = new ArrayList<>();
        for (Follow follow: followEachOtherList) {
            result.add(userService.getUserById(follow.getMyKey().getId_user_followed()));
        }
        return result;
    }

//    public List<Hashtable<String, Object>> getListFriendUser(Long id_user_follow){
//        try{
//            List<Object> follow = followRepository.getListFriend();
//            List<Hashtable<String, Object>> friends = new ArrayList<>();
//            for (Object fl : follow){
//                if (Objects.equals(((Object[]) fl)[0], id_user_follow)) {
//                    Hashtable<String,Object> friend = new Hashtable<>();
//                    friend.put("id_user_follow", ((Object[]) fl)[0]);
//                    friend.put("id_user_followed", ((Object[]) fl)[1]);
//                    friends.add(friend);
//                }
//            }
//            return friends;
//        } catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }

//    public Boolean isFriend(Long id_user_follow, Long id_user_followed){
//        try{
//            List<Object> getFriend = followRepository.getListFriend();
//            for (Object fl : getFriend){
//                if (Objects.equals(((Object[]) fl)[0], id_user_follow) && Objects.equals(((Object[]) fl)[0], id_user_followed)){
//                    return true;
//                }
//            }
//            return false;
//        } catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//    }
}
