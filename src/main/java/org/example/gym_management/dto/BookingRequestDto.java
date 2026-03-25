package org.example.gym_management.dto;

import lombok.Data;

@Data
public class BookingRequestDto {
    private long clientId;
    private long gymClassId;
}
