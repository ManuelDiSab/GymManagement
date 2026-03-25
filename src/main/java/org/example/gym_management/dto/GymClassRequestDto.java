package org.example.gym_management.dto;

import lombok.Data;

import java.time.LocalDateTime;
 @Data
public class GymClassRequestDto {

    private String name;
    private String description;
    private int nPlaces;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long instructorId;
}
