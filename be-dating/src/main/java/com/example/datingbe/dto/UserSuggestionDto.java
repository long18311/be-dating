package com.example.datingbe.dto;

import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSuggestionDto {
    private Long id;
    private String city;
    private double latitude;
    private double longitude;
    List<Long> information_id;
}