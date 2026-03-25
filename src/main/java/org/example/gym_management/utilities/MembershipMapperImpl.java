package org.example.gym_management.utilities;
import org.example.gym_management.dto.MembershipResponseDto;
import org.example.gym_management.dto.UserResponseDto;
import org.example.gym_management.entities.Membership;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MembershipMapperImpl implements MembershipMapper {
    @Override
    public MembershipResponseDto toDto(Membership membership) {
        if(membership == null) return null;
        MembershipResponseDto dto = new MembershipResponseDto();
        dto.setStartDate(membership.getStartDate());
        dto.setEndDate(membership.getEndDate());
        dto.setSubType(membership.getSubType());
        if (membership.getUser() != null) {
            UserResponseDto userDto = new UserResponseDto();
            userDto.setId(membership.getUser().getId());
            userDto.setUsername(membership.getUser().getUsername());
            userDto.setEmail(membership.getUser().getEmail());
            dto.setUser(userDto);
        }
        return dto;
    }

    @Override
    public List<MembershipResponseDto> toDtoList(List<Membership> memberships) {
        if(memberships == null) return null;
        return memberships.stream().map(this::toDto).toList();
    }
}
