package org.example.gym_management.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private long id;
    private UserResponseDto user;
    private GymClassResponseDto gymClass;
    private LocalDateTime createdAt;
}
