package com.example.social_network_fpt_be.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class FollowKey implements Serializable {

    @Column(name = "id_user_follow", nullable = false)
    private Long id_user_follow;

    @Column(name = "id_user_followed", nullable = false)
    private Long id_user_followed;

    public MyKey(Long id_user_follow, Long id_user_followed) {
        this.id_user_follow = id_user_follow;
        this.id_user_followed = id_user_followed;
    }

    public MyKey() {

    }
}
