package com.example.datingbe.rest;
import com.example.datingbe.entity.Notification;
import com.example.datingbe.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private final NotificationService notificationService;
    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @GetMapping("/notifications")
        public Page<Notification> getNotificationsForUser(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return notificationService.getNotificationsForUser(userId, PageRequest.of(page, size, Sort.by("timestamp").descending()));
    }
        @GetMapping("/unread-count")
        public ResponseEntity<Long> getUnreadNotificationCount(@RequestParam Long recipientId) {
        Long unreadCount = notificationService.countUnreadNotifications(recipientId);
        return ResponseEntity.ok(unreadCount);
    }
    @PutMapping("/mark-as-read/{notificationId}")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long notificationId, @RequestParam Long recipientId) {
        try {
            notificationService.markNotificationAsRead(notificationId, recipientId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/mark-all-as-read")
    public ResponseEntity<?> markAllNotificationsAsRead(@RequestParam Long recipientId) {
        try {
            notificationService.markAllNotificationsAsRead(recipientId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
