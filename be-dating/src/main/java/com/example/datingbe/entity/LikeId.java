package com.example.datingbe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeId implements Serializable {
//    @Column(name = "user_id")
    private Long user_id;
//    @Column(name = "post_id")
    private Long post_id;

}
