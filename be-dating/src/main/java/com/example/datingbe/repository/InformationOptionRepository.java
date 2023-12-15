package com.example.datingbe.repository;

import com.example.datingbe.entity.InformationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InformationOptionRepository extends JpaRepository<InformationOption, Long> {
    @Query(value = "select u.informationOptions from User u where u.id = ?1")
    public List<InformationOption> getInformationUserId(Long id);
}