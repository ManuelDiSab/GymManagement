package org.example.gym_management.utilities;

import org.example.gym_management.dto.BookingResponseDto;
import org.example.gym_management.dto.GymClassResponseDto;
import org.example.gym_management.dto.UserResponseDto;
import org.example.gym_management.entities.Booking;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public BookingResponseDto toDto(Booking booking) {
        if (booking == null) return null;
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setCreatedAt(booking.getCreatedAt());
        if (booking.getClient() != null) {
            UserResponseDto userDto = new UserResponseDto();
            userDto.setId(booking.getClient().getId());
            userDto.setUsername(booking.getClient().getUsername());
            userDto.setEmail(booking.getClient().getEmail());
            dto.setUser(userDto);
        }
        if (booking.getGymClass() != null) {
            GymClassResponseDto classDto = new GymClassResponseDto();
            classDto.setId(booking.getGymClass().getId());
            classDto.setName(booking.getGymClass().getName());
            classDto.setDescription(booking.getGymClass().getDescription());
            classDto.setStartDate(booking.getGymClass().getStartDate());
            dto.setGymClass(classDto);
        }
        return dto;
    }

    @Override
    public List<BookingResponseDto> toDtoList(List<Booking> bookings) {
        if(bookings == null) return null;
        return bookings.stream().map(this::toDto).toList();
    }
}
