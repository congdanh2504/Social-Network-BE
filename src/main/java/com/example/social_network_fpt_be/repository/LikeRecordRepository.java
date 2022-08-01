package com.example.social_network_fpt_be.repository;

import com.example.social_network_fpt_be.models.LikeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRecordRepository extends JpaRepository<LikeRecord, Long> {

    @Query(value = "SELECT l " +
            "FROM LikeRecord l " +
            "WHERE l.likeKey.id_target = :id_post AND l.likeKey.type = 'post'")
    public List<LikeRecord> getLikeRecordsByIdPost(Long id_post);

    @Query(value = "SELECT l " +
            "FROM LikeRecord l " +
            "WHERE l.likeKey.id_target = :id_comment AND l.likeKey.type = 'comment'")
    public List<LikeRecord> getLikeRecordsByIdComment(Long id_comment);
}
