package com.example.datingbe.repository;

import com.example.datingbe.entity.Like;
import com.example.datingbe.entity.LikeId;
import com.example.datingbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {
    // Đếm số lượng like của một bài viết
    @Query("SELECT COUNT(l) FROM Like l WHERE l.post.id = :postId")
    Long countLikesByPostId(Long postId);

    // Lấy danh sách người dùng đã like bài viết
    @Query("SELECT l.user FROM Like l WHERE l.post.id = :postId")
    List<User> findUsersLikedPost(Long postId);
    @Query("SELECT l FROM Like l WHERE l.user.id = :userId AND l.post.id = :postId")
    Like getLikeByUserAndPost(@Param("userId") Long userId,@Param("postId") Long postId);

}