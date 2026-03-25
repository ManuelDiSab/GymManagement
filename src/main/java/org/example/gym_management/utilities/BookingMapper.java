package org.example.gym_management.utilities;

import org.example.gym_management.dto.BookingResponseDto;
import org.example.gym_management.entities.Booking;

import java.util.List;

public interface BookingMapper {
    BookingResponseDto toDto(Booking booking);
    List<BookingResponseDto> toDtoList(List<Booking> bookings);
}
