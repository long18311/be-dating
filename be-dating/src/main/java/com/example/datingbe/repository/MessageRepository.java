package com.example.datingbe.repository;

import com.example.datingbe.dto.UserMeDto;
import com.example.datingbe.entity.Message;
import com.example.datingbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value ="SELECT m FROM Message m WHERE (m.sender.id = :sen AND m.receiver.id = :rec) OR (m.sender.id = :rec AND m.receiver.id = :sen)")
    List<Message> findByUsers(@Param("sen") Long sen, @Param("rec") Long rec);
    @Query("SELECT new com.example.datingbe.dto.UserMeDto(u.id, CONCAT(u.lastname, ' ', u.firstname), u.avatar, m.content, m.timestamp, COUNT(m2.id)) " +
            "FROM User u " +
            "LEFT JOIN Message m ON (u = m.sender OR u = m.receiver) AND m.status != 2 " +
            "LEFT JOIN Message m2 ON (u = m2.sender OR u = m2.receiver) AND m2.status = 0 AND (m2.receiver.id = :currentUserId) " +
            "WHERE ((u = m.sender AND m.receiver.id = :currentUserId) OR (u = m.receiver AND m.sender.id = :currentUserId)) " +
            "AND u.id != :currentUserId " +
            "AND m.timestamp = (SELECT MAX(m3.timestamp) FROM Message m3 WHERE (m3.sender = u OR m3.receiver = u) AND (m3.sender.id = :currentUserId OR m3.receiver.id = :currentUserId) AND m3.status != 2) " +
            "GROUP BY u.id, u.username, u.avatar, m.content, m.timestamp")
    List<com.example.datingbe.dto.UserMeDto> getUserMessage(@Param("currentUserId") Long userId);
    @Query("SELECT COUNT(DISTINCT u.id) " +
            "FROM User u " +
            "JOIN Message m ON (u = m.sender OR u = m.receiver) " +
            "WHERE (m.status = 0 AND m.receiver.id = :currentUserId) " +
            "AND u.id != :currentUserId")
    Long countUnreadConversations(@Param("currentUserId") Long currentUserId);
    @Transactional
    @Modifying
    @Query("UPDATE Message m SET m.status = 1 WHERE  (m.sender = :rec AND m.receiver = :sen) AND m.status = 0")
    int updateMessagesStatus(@Param("sen") User sen, @Param("rec") User rec);
    @Modifying
    @Query("UPDATE Message m SET m.status = 2,m.content = :content WHERE m.id = :id")
    int recallMessage(@Param("id")Long id,@Param("content") String content);

}