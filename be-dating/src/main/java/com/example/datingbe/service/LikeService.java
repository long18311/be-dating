package com.example.datingbe.service;

import com.example.datingbe.entity.Like;
import com.example.datingbe.entity.LikeId;
import com.example.datingbe.entity.Post;
import com.example.datingbe.entity.User;
import com.example.datingbe.repository.LikeRepository;
import com.example.datingbe.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikeRepository likeRepository;
    public Like addLikePost(User user, Long postId, boolean isLiked) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()) {
            // Xử lý trường hợp không tìm thấy Post, ví dụ ném ra ngoại lệ
            return null;
        }

        Post post = postOptional.get();
        System.out.println(post);
        Like like = likeRepository.getLikeByUserAndPost(user.getId(), post.getId());
        System.out.println(like);
        if (like == null) {
            System.out.println("vào rồi");
            like = new Like(); // Khởi tạo đối tượng Like mới
            like.setId(new LikeId(user.getId(),post.getId()));
            like.setUser(user);
            like.setPost(post);

            System.out.println("Like is :"+like);
        }

        like.setIsLiked(isLiked);
        System.out.println("user=" + like.getUser().getFirstname());
        System.out.println("post=" + like.getPost().getContent());
        like = likeRepository.save(like);
        return like;
    }

    public int deleteLikePost(User user, Long postId){
        Optional<Like> likeOptional = likeRepository.findById(new LikeId(user.getId(),postId));
        if(likeOptional.isPresent()){
            likeRepository.deleteById(new LikeId(user.getId(),postId));
            return 1;
        } else return 0;
    }
}
