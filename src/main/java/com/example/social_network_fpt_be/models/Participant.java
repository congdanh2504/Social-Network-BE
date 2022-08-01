package com.example.social_network_fpt_be.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "participant")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id_user;
    @Column(name = "role")
    private int role;
    @ManyToMany(mappedBy = "conversation_list")
    private Set<Conversation> list_conversation;
}
