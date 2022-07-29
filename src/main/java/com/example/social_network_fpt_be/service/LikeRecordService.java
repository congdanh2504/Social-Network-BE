package com.example.social_network_fpt_be.service;

import com.example.social_network_fpt_be.repository.LikeRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class LikeRecordService {

    private final LikeRecordRepository likeRecordRepository;

    @Autowired
    public LikeRecordService(LikeRecordRepository likeRecordRepository) {
        this.likeRecordRepository = likeRecordRepository;
    }

    public int getLikeCountByIdPost(Long post_id) {
        return likeRecordRepository.getLikeRecordsByIdPost(post_id).size();
    }
}
