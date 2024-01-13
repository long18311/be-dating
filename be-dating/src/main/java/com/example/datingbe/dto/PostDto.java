package com.example.datingbe.dto;

import com.example.datingbe.entity.Like;
import com.example.datingbe.entity.Post;
import com.example.datingbe.entity.PostImage;
import com.example.datingbe.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@Data
@NoArgsConstructor
public class PostDto {
    private Long id;
    private UserDto user;
    private String title;
    private String content;
    private Date createDate;
    private Set<PostImage> images;
    private int islike=0;
    private String type;
//    private int countLike;
//    private int countDislike;
    private Set<UserDto> userLikes;
    private Set<UserDto> userDislikes;
    public PostDto(Post post, User user) {
        this.id = post.getId();
        this.user = new UserDto(post.getUser());
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createDate = post.getCreateDate();
        this.images = post.getImages();
        this.type = post.getType();
//        this.countLike = 0;
//        this.countDislike = 0;
        userLikes = new HashSet<>();
        userDislikes = new HashSet<>();
        islike=0; // Giả sử là 0 nếu chưa like hoặc dislike
        if(post.getLikes()!=null&&post.getLikes().size()>0) {
            for (Like like : post.getLikes()) {
                if (like.getIsLiked()) {
//                countLike++;
                    userLikes.add(new UserDto(like.getUser()));
                    if (like.getUser().equals(user)) {
                        islike = 1; // Người dùng đã like bài viết
                    }
                } else {
//                countDislike++;
                    userDislikes.add(new UserDto(like.getUser()));
                    if (like.getUser().equals(user)) {
                        islike = 2; // Người dùng đã dislike bài viết
                    }
                }
            }
        }

    }

    public PostDto(Post post) {
        this.id = post.getId();
        this.user = new UserDto(post.getUser());
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createDate = post.getCreateDate();
        this.images = post.getImages();
        this.type = post.getType();
//        this.countLike = 0;
//        this.countDislike = 0;
        userLikes = new HashSet<>();
        userDislikes = new HashSet<>();
        if(post.getLikes()!=null&&post.getLikes().size()>0) {
            for (Like like : post.getLikes()) {
                if (like.getIsLiked()) {
//                countLike++;
                    userLikes.add(new UserDto(like.getUser()));
                } else {
//                countDislike++;
                    userDislikes.add(new UserDto(like.getUser()));
                }
            }
        }

    }
}
