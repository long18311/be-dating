package com.example.datingbe.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "likes")
@Getter
@Setter

public class Like implements Serializable {
    @EmbeddedId
    private LikeId id;
    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @MapsId("post_id")
    @JoinColumn(name = "post_id" , referencedColumnName = "id")
    private Post post;
    @Column(nullable = false)
    private Boolean isLiked;

    public Like() {
    }

    public Like( User user, Post post, Boolean isLiked) {
        this.user = user;
        this.post = post;
        this.isLiked = isLiked;
    }
}
