package com.example.datingbe.service;

import com.example.datingbe.entity.Notification;
import com.example.datingbe.entity.User;
import com.example.datingbe.entity.UserReport;
import com.example.datingbe.repository.UserReportRepository;
import com.example.datingbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserReportService {
    @Autowired
    private UserReportRepository userReportRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserRepository userRepository;

    // Tạo một báo cáo mới
    @Transactional
    public UserReport createReport(UserReport newReport) {
        return userReportRepository.save(newReport);
    }

    // Tìm tất cả báo cáo theo loại
    public List<UserReport> findReportsByType(String reportType) {
        return userReportRepository.findByReportType(reportType);
    }

    // Tìm tất cả báo cáo theo trạng thái
    public List<UserReport> findReportsByStatus(String status) {
        return userReportRepository.findByStatus(status);
    }

    // Cập nhật trạng thái của một báo cáo
    @Transactional
    public int updateReportStatus(Long reportId, String status) {
        return userReportRepository.updateReportStatus(reportId, status);
    }

    public int updateUserReportDetails(Long reportId, String status, String adminNotes, User userHandlingReport) {
        try {
        UserReport userReport = userReportRepository.getReferenceById(reportId);
        if (userReport == null){ return 2;}
        userReport.setAdminNotes(adminNotes);
        userReport.setStatus(status);
        userReport.setUserHandlingReport(userHandlingReport);
        userReport = userReportRepository.save(userReport);
        return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0; // Lưu thất bại
        }
    }
    @Transactional
    public int updateReportAccepted(Long reportId, String adminNotes, User userHandlingReport) {
        try {
            UserReport userReport = userReportRepository.getReferenceById(reportId);
            if (userReport == null){ return 2;}
            userReport.setAdminNotes(adminNotes);
            userReport.setStatus("accepted");
            userReport.setUserHandlingReport(userHandlingReport);
            userReport = userReportRepository.save(userReport);
            User user = userReport.getReportedUser();
            user.setActived(0);
            user = userRepository.save(user);
            Notification notification = new Notification();
            notification.setMessage(userReport.getAdminNotes());
            notification.setStatus(0);
            notification.setSender(userReport.getReportedUser()); // Đặt người gửi là người chấp nhận lời mời
            notification.setRecipient(userReport.getReportedBy()); // Đặt người nhận là người đã gửi lời mời
            notification.setTimestamp(new Timestamp(System.currentTimeMillis()));
            notificationService.saveNotification(notification);
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0; // Lưu thất bại
        }
    }
    @Transactional
    public int updateReportRejected(Long reportId, String adminNotes, User userHandlingReport) {
        try {
            UserReport userReport = userReportRepository.getReferenceById(reportId);
            if (userReport == null){ return 2;}
            userReport.setAdminNotes(adminNotes);
            userReport.setStatus("rejected");
            userReport.setUserHandlingReport(userHandlingReport);
            userReport = userReportRepository.save(userReport);
            User user = userReport.getReportedUser();
            user.setActived(1);
            user = userRepository.save(user);
            Notification notification = new Notification();
            notification.setMessage(userReport.getAdminNotes());
            notification.setStatus(0);
            notification.setSender(userReport.getReportedUser()); // Đặt người gửi là người chấp nhận lời mời
            notification.setRecipient(userReport.getReportedBy()); // Đặt người nhận là người đã gửi lời mời
            notification.setTimestamp(new Timestamp(System.currentTimeMillis()));
            notificationService.saveNotification(notification);
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0; // Lưu thất bại
        }
    }
    public Page<UserReport> getReportPagebyStatusandReportType(String status,String reportType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if(status.equalsIgnoreCase("all")){
            return userReportRepository.findByReportType(reportType, pageable);
        }

        return userReportRepository.getReportPagebyStatusandReportType(status,reportType, pageable);
    }

    // Trong UserReportService.java
    public Page<UserReport> findReportsByTypeWithPagination(String reportType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userReportRepository.findByReportType(reportType, pageable);
    }
    // Trong UserReportService.java
    public Optional<UserReport> findReportById(Long id) {
        return userReportRepository.findById(id);
    }


}

