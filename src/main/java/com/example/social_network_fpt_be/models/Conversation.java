package com.example.social_network_fpt_be.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "conversation")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conversation")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "id_creator")
    private Long id_creator;
    @Column(name = "create_date")
    private String create_date;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "conversation_list",
            joinColumns = @JoinColumn(name = "id_conversation"),
            inverseJoinColumns = @JoinColumn(name = "id_user")
    )
    private Set<Participant> conversation_list;
}
