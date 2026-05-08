package org.example.gym_management.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
 @Data
public class GymClassRequestDto {

     @NotBlank(message = "Class name is required")
     private String name;

     private String description;
     @Min(value = 1,message = "Should have at least 1 place")
     @Max(value = 20,message = "Should have maximum 20 place")
     private int nPlaces;

     @NotNull(message = "Start date is required")
     @Future(message = "Start date should be in the future")
     private LocalDateTime startDate;

     @NotNull(message = "End date is required")
     @Future(message = "End date should be in the future")
     private LocalDateTime endDate;

     @NotNull(message = "The instructor is required")
     @Positive(message = "The instructor id must be a positive number")
     private Long instructorId;
 }
