package com.example.social_network_fpt_be.service;

import com.example.social_network_fpt_be.models.LikeRecord;
import com.example.social_network_fpt_be.repository.LikeRecordRepository;
import com.example.social_network_fpt_be.service.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LikeRecordService {

    private final LikeRecordRepository likeRecordRepository;
    private final UserService userService;

    @Autowired
    public LikeRecordService(LikeRecordRepository likeRecordRepository,@Lazy UserService userService) {
        this.likeRecordRepository = likeRecordRepository;
        this.userService = userService;
    }

    public List<LikeRecord> getLikeRecordByPostId(Long post_id) {
        return likeRecordRepository.getLikeRecordsByIdPost(post_id);
    }

    public List<UserDto> getLikeUsersByPostId(Long post_id) {
        List<LikeRecord> likeRecords = likeRecordRepository.getLikeRecordsByIdPost(post_id);
        return likeRecords.stream().map((likeRecord -> userService.getUserById(likeRecord.getLikeKey().getId_user()))).collect(Collectors.toList());
    }
}
