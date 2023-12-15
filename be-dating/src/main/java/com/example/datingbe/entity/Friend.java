package com.example.datingbe.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
@Entity
@Table(name = "Friends")
@Data
public class Friend{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_user_id ", referencedColumnName = "id")
    private User sender_user_id;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id ", referencedColumnName = "ID")
    private User receiver_user_id ;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('pending', 'accepted', 'rejected') DEFAULT 'pending'")
    private FriendStatus status = FriendStatus.pending;

    @Column(name = "createdAt", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;
}
