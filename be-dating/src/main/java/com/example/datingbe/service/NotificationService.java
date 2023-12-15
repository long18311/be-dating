package com.example.datingbe.service;
import com.example.datingbe.entity.Notification;
import com.example.datingbe.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Page<Notification> getNotificationsForUser(Long userId, Pageable pageable) {
        return notificationRepository.findByRecipientId(userId, pageable);
    }
    public Optional<Notification> getNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId);
    }

    public Notification saveNotification(Notification notification) {

        notification = notificationRepository.save(notification);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(notification.getRecipient().getId()),
                "/queue/notifications",
                notification.getSender().getId()
        );
        return notification;
    }

    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
    public Long countUnreadNotifications(Long recipientId) {
        return notificationRepository.countByRecipientIdAndUnreadStatus(recipientId);
    }
    public void markNotificationAsRead(Long notificationId, Long recipientId) {
        notificationRepository.markNotificationAsRead(notificationId, recipientId);
    }

    public void markAllNotificationsAsRead(Long recipientId) {
        notificationRepository.markAllAsRead(recipientId);
    }
}