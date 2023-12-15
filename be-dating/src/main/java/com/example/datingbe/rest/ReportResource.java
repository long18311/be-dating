package com.example.datingbe.rest;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.datingbe.entity.Images;
import com.example.datingbe.entity.InformationOption;
import com.example.datingbe.entity.User;
import com.example.datingbe.entity.UserReport;
import com.example.datingbe.service.UserReportService;
import com.example.datingbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/report/")
public class ReportResource {
    @Autowired
    private  UserReportService userReportService;
    @Autowired
    private UserService userService;
    @Autowired
    private Cloudinary cloudinary;

    // API để lấy tất cả báo cáo theo trạng thái
    @GetMapping("/{reportId}")
    public ResponseEntity<UserReport> getReportsByStatus(@PathVariable Long reportId) {
        Optional<UserReport> reports = userReportService.findReportById(reportId);
        if (reports.isPresent()) {
            return ResponseEntity.ok().body(reports.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<UserReport>> getReportsByStatus(@PathVariable String status) {
        List<UserReport> reports = userReportService.findReportsByStatus(status);
        return ResponseEntity.ok().body(reports);
    }

    // API để tạo báo cáo mới
    @PostMapping("/send-report")
    public ResponseEntity<String> sendReport(
            @RequestParam("idReport") Long idReport,
            @RequestParam("reportType") String reportType,
            @RequestPart("evidence") MultipartFile evidence,
            @RequestParam("reportContent") String reportContent
    ) {
        try {
            UserReport report = new UserReport();
            report.setReportedBy(userService.getUserWithAuthority());
            report.setReportLink(null);
            report.setReportedUser(userService.getOne(idReport));
            report.setReportContent(reportContent);
            report.setReportType(reportType);
            report.setReportDate(new Date());
            report.setStatus("pending");
            Map uploadResult = cloudinary.uploader().upload(evidence.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto","folder", "report" // Dùng "video" làm resource_type cho cả âm thanh và video
            ));
            report.setEvidenceImage((String)uploadResult.get("url"));
            report = userReportService.createReport(report);

           // Xử lý y hệt hồ sơ

            return new ResponseEntity<>("Hồ sơ của bạn đã được cập nhật", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Lỗi :"+ e.getMessage());
            return new ResponseEntity<>("Lỗi cập nhật hồ sơ , vui lòng liên hệ admin", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API để cập nhật trạng thái báo cáo
    @PutMapping("/status/{id}")
    public ResponseEntity<Void> updateReportStatus(
            @PathVariable Long id,
            @RequestParam("status") String status,
            @RequestParam("adminNotes") String adminNotes) {
        User userHandlingReport = userService.getUserWithAuthority();
        int result = userReportService.updateUserReportDetails(id, status, adminNotes, userHandlingReport);
        if (result == 1) {
            return ResponseEntity.ok().build();
        } else if(result == 2) {
            return  ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.internalServerError().build();
        }

    }
    @PutMapping("/accepted/{id}")
    public ResponseEntity<Void> updateReportAccepted(
            @PathVariable Long id,
            @RequestParam("adminNotes") String adminNotes) {
        User userHandlingReport = userService.getUserWithAuthority();
        int result = userReportService.updateReportAccepted(id, adminNotes, userHandlingReport);
        if (result == 1) {
            return ResponseEntity.ok().build();
        } else if(result == 2) {
            return  ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.internalServerError().build();
        }

    }
    @PutMapping("/rejected/{id}")
    public ResponseEntity<Void> updateReportRejected(
            @PathVariable Long id,
            @RequestParam("adminNotes") String adminNotes) {
        User userHandlingReport = userService.getUserWithAuthority();
        int result = userReportService.updateReportRejected(id, adminNotes, userHandlingReport);
        if (result == 1) {
            return ResponseEntity.ok().build();
        } else if(result == 2) {
            return  ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.internalServerError().build();
        }

    }

    // Trong UserReportController.java

    @GetMapping("/type/{reportType}")
    public ResponseEntity<Page<UserReport>> getReportsByType(
            @PathVariable String reportType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<UserReport> reportPage = userReportService.findReportsByTypeWithPagination(reportType, page, size);
        return ResponseEntity.ok().body(reportPage);
    }
    @GetMapping("/{reportType}/{status}")
    public ResponseEntity<Page<UserReport>> getReportsByTypeandStatus(
            @PathVariable String status,
            @PathVariable String reportType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<UserReport> reportPage = userReportService.getReportPagebyStatusandReportType(status,reportType, page, size);
        return ResponseEntity.ok().body(reportPage);
    }
}
