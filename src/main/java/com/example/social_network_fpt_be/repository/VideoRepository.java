package com.example.social_network_fpt_be.repository;

import com.example.social_network_fpt_be.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VideoRepository extends JpaRepository<Video, Long>{

}
