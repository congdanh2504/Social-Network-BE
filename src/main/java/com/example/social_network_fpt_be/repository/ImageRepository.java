package com.example.social_network_fpt_be.repository;

import com.example.social_network_fpt_be.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer>{

    @Query("SELECT i.id_image, i.url, i.create_date, i.type, i.id " +
            "FROM Image i "+
            "WHERE i.type = ?1 and i.id = ?2")
    List<Object> findImageByTypeAndId(String type, int id);


}
