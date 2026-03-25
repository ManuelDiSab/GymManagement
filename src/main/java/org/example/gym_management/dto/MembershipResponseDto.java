package org.example.gym_management.dto;

import lombok.Data;
import org.example.gym_management.entities.EMembership;

import java.time.LocalDate;
@Data
public class MembershipResponseDto {
    private UserResponseDto user;
    private EMembership subType;
    private LocalDate startDate;
    private LocalDate endDate;
}
