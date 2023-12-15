package com.example.datingbe.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_reports")
@Data
public class UserReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "reported_by", nullable = false)
    private User reportedBy;
    @ManyToOne
    @JoinColumn(name = "reported_user", nullable = false)
    private User reportedUser;
    @ManyToOne
    @JoinColumn(name = "user_handling_report",nullable = true)
    private User userHandlingReport;
    @Column(nullable = false)
    private String reportType; // giả mạo or lạm dụng

    @Column
    private String reportLink;

    @Column(nullable = false, length = 4000)
    private String reportContent;

    @Column
    private String evidenceImage;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportDate;

    @Column
    private String status; // pending -> CHƯA XỬ LÝ, accepted -> ĐÃ XỬ LÝ ( KHOÁ ), rejected -> ĐÃ XỬ LÝ ( KHÔNG LOCK )

    @Column
    private String adminNotes;
}
