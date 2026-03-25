package org.example.gym_management.dto;

import lombok.Data;
import org.example.gym_management.entities.EMembership;

import java.time.LocalDate;

@Data
public class MembershipRequestDto {
    private Long idUser;
    private EMembership subType;
    private LocalDate startDate;

}
