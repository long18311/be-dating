package com.example.datingbe.repository;

import com.example.datingbe.entity.Post;
import com.example.datingbe.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {


}