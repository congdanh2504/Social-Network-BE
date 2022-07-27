package com.example.social_network_fpt_be.service;

import com.example.social_network_fpt_be.models.Conversation;
import com.example.social_network_fpt_be.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;
    public void saveConversation(Conversation conversation){
        this.conversationRepository.save(conversation);
    }
}
