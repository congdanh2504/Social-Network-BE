package com.example.social_network_fpt_be.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class LikeKey implements Serializable {

    @Column(name = "id_target", nullable = false)
    private Long id_target;

    @Column(name = "id_user", nullable = false)
    private Long id_user;

    @Column(name = "type", nullable = false)
    private String type;
}