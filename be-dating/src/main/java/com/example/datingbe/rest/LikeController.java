package com.example.datingbe.rest;

import com.example.datingbe.dto.PostDto;
import com.example.datingbe.entity.Like;
import com.example.datingbe.entity.User;
import com.example.datingbe.service.LikeService;
import com.example.datingbe.service.PostService;
import com.example.datingbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/like")
public class LikeController {
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private PostService postService;
    @PostMapping("like/{postId}")
    public ResponseEntity<String> addLike(@PathVariable long postId){
        User user = userService.getUserWithAuthority();
        Like like = likeService.addLikePost(user, postId,true);
        if(like == null){
            return ResponseEntity.internalServerError().body("Không tìm được Post");
        }else {
            return ResponseEntity.ok("Thành công");
        }
    }
    @PostMapping("dislike/{postId}")
    public ResponseEntity<?> addDislike(@PathVariable long postId){
        User user = userService.getUserWithAuthority();
        Like like = likeService.addLikePost(user, postId,false);
        if(like == null){
            return ResponseEntity.internalServerError().body("Không tìm được Post");
        }else {
            return ResponseEntity.ok("Thành công");
        }
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteLike(@PathVariable long postId){
        User user = userService.getUserWithAuthority();
        int result = likeService.deleteLikePost(user, postId);
        if(result == 1){
            return ResponseEntity.ok("Thành công");
        }else {
            return ResponseEntity.internalServerError().body("Không tìm được Post");
        }
    }

}
