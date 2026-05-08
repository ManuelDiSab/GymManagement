package org.example.gym_management.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequestDto {
    @NotNull(message = "Client ID is required")
    @Positive(message = "The value has to be a positive number")
    private Long clientId;

    @NotNull(message = "Class ID is required")
    @Positive(message = "The value has to be a positive number")
    private Long gymClassId;
}
