package org.example.gym_management.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.example.gym_management.entities.EMembership;

import java.time.LocalDate;

@Data
public class MembershipRequestDto {
    @NotNull(message = "The user is required")
    @Positive(message = "The user id must be a positive number")
    private Long idUser;

    @NotNull(message = "The membership type is required")
    private EMembership subType;

    @FutureOrPresent(message = "The start date cannot be in the past!!")
    private LocalDate startDate;

}
