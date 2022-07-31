package com.example.social_network_fpt_be.repository;

import com.example.social_network_fpt_be.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{

    @Query("SELECT i " +
            "FROM Image i "+
            "WHERE i.type = ?1 and i.id = ?2")
    List<Image> findImageByTypeAndId(String type, Long id);

    @Query("SELECT i FROM Image i WHERE i.type = 'user_avt' AND i.id = :userId")
    Image getAvatarByUser(Long userId);

    @Query("SELECT i FROM Image i WHERE i.type = 'user_cover' AND i.id = :userId")
    Image getCoverImageByUser(Long userId);
}
