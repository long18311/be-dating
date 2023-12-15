package com.example.datingbe.repository;

import com.example.datingbe.entity.UserReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    // Trong UserReportRepository.java
    Optional<UserReport> findById(Long id);


    // Truy vấn tìm tất cả báo cáo của một người dùng cụ thể
    @Query("SELECT ur FROM UserReport ur WHERE ur.reportedBy.id = :userId")
    List<UserReport> findAllReportsByUserId(Long userId);

    // Truy vấn tìm tất cả báo cáo về một người dùng cụ thể
    @Query("SELECT ur FROM UserReport ur WHERE ur.reportedUser.id = :userId")
    List<UserReport> findAllReportsAboutUserId(Long userId);

    // Tìm tất cả báo cáo theo loại
    @Query("SELECT ur FROM UserReport ur WHERE ur.reportType = :reportType")
    List<UserReport> findByReportType(String reportType);

    // Tìm báo cáo theo trạng thái xét duyệt
    @Query("SELECT ur FROM UserReport ur WHERE ur.status = :status")
    List<UserReport> findByStatus(String status);

    // Cập nhật trạng thái báo cáo
    @Modifying
    @Query("UPDATE UserReport ur SET ur.status = :status WHERE ur.id = :id")
    int updateReportStatus(Long id, String status);

    // Trong UserReportRepository.java
    @Query("SELECT ur FROM UserReport ur WHERE ur.reportType = :reportType")
    Page<UserReport> findByReportType(String reportType, Pageable pageable);
    @Query("SELECT ur FROM UserReport ur WHERE ur.reportType = :reportType and ur.status = :status")
    Page<UserReport> getReportPagebyStatusandReportType(String status,String reportType, Pageable pageable);


}
