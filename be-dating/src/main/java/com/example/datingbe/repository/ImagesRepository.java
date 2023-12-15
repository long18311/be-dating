package com.example.datingbe.repository;

import com.example.datingbe.entity.Images;
import com.example.datingbe.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<Images,Long> {
    @Query(value ="SELECT i FROM Images i WHERE i.user.id = :userId")
    List<Images> findByUsers(@Param("userId") Long userId);
}
