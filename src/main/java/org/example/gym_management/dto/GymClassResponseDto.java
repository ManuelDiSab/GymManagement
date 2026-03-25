package org.example.gym_management.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GymClassResponseDto {
    private long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
}
