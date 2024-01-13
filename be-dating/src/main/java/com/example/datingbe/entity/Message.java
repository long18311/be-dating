package com.example.datingbe.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "message")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User receiver;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    //    0 là chưa xem, 1 là đã xem,2 là đã thu hồi
    @Column(name = "status", columnDefinition = "INT DEFAULT 0")
    private Integer status;
    @Column(name = "timestamp")
    private Date timestamp;
}
