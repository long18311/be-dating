package com.example.datingbe.entity;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "conversation")
@Data
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "participant1_id")
    private User participant1;

    @ManyToOne
    @JoinColumn(name = "participant2_id")
    private User participant2;

    @Column(name = "last_message")
    private String lastMessage;

    @Column(name = "timestamp")
    private Date timestamp;
}
