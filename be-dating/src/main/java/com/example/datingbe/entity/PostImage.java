package com.example.datingbe.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
@Entity
@Table(name = "post_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "postId", nullable = false)
    private Post post;
    private String imageUrl;
//    private String description;

    public PostImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
