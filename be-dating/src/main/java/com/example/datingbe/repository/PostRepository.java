package com.example.datingbe.repository;

import com.example.datingbe.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Cập nhật bài viết
    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.title = :title, p.content = :content WHERE p.id = :postId")
    void updatePost(Long postId, String title, String content);
    @Query("SELECT p FROM Post p ORDER BY p.createDate DESC")
    Page<Post> findAllByOrderByCreateDateDesc(Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.type = :type")
    List<Post> findByType(Sort sort,String type);



}