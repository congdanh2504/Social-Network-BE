package com.example.social_network_fpt_be.service;

import com.example.social_network_fpt_be.models.Participant;
import com.example.social_network_fpt_be.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;

    public Participant getParticipantById(Long Id){
        return participantRepository.findById(Id).orElse(null);
    }
}
