package com.example.datingbe.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;
    private String content;
    private String type;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @JsonManagedReference
    @OneToMany(mappedBy = "post")
    private Set<PostImage> images;

    @OneToMany(mappedBy = "post")
    private Set<Like> likes;

}
