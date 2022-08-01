package com.example.social_network_fpt_be.controller;

import com.example.social_network_fpt_be.models.Participant;
import com.example.social_network_fpt_be.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/conversation")
public class ConversationController {
    @Autowired
    private ParticipantService participantService;

    @GetMapping("/")
    private Participant getPaticipantById(@PathVariable Long id){
        return  participantService.getParticipantById(id);
    }
}
