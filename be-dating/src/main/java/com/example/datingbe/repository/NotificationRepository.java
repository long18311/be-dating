package com.example.datingbe.repository;

import com.example.datingbe.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByRecipientId(Long recipientId, Pageable pageable);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.recipient.id = :recipientId AND n.status = 0")
    Long countByRecipientIdAndUnreadStatus(@Param("recipientId") Long recipientId);
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.status = '1' WHERE n.recipient.id = :recipientId AND n.status = 0")
    void markAllAsRead(@Param("recipientId") Long recipientId);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.status = '1' WHERE n.id = :notificationId AND n.recipient.id = :recipientId AND n.status = 0")
    void markNotificationAsRead(@Param("notificationId") Long notificationId, @Param("recipientId") Long recipientId);

}