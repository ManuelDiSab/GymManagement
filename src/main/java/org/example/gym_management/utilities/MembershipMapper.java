package org.example.gym_management.utilities;

import org.example.gym_management.dto.BookingResponseDto;
import org.example.gym_management.dto.MembershipResponseDto;
import org.example.gym_management.entities.Booking;
import org.example.gym_management.entities.Membership;

import java.util.List;

public interface MembershipMapper {
    public MembershipResponseDto toDto(Membership membership);
    public List<MembershipResponseDto> toDtoList(List<Membership> memberships);
}
