package com.example.social_network_fpt_be.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowKey implements Serializable {

    @Column(name = "id_user_follow", nullable = false)
    private Long id_user_follow;

    @Column(name = "id_user_followed", nullable = false)
    private Long id_user_followed;
}
