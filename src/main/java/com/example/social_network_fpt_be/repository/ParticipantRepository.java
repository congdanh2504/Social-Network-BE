package com.example.social_network_fpt_be.repository;

import com.example.social_network_fpt_be.models.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

}
