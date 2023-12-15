package com.example.datingbe.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter
@Setter
@EqualsAndHashCode
    public class Notification {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;
        private String message;
        private Timestamp timestamp;
        @ManyToOne
        @JoinColumn(name = "sender_id")
        private User sender;
        @ManyToOne
        @JoinColumn(name = "recipient_id")
        private User recipient;
        @Column(name = "status", columnDefinition = "INT DEFAULT 0")
        private Integer status;
        // ... các thuộc tính khác nếu cần
    }

