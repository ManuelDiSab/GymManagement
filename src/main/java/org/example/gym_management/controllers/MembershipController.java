package org.example.gym_management.controllers;
import org.example.gym_management.dto.MembershipRequestDto;
import org.example.gym_management.entities.Booking;
import org.example.gym_management.entities.Membership;
import org.example.gym_management.services.MembershipServiceImpl;
import org.example.gym_management.services.UserServiceImpl;
import org.example.gym_management.utilities.MembershipMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/membership")
public class MembershipController {

    @Autowired
    MembershipServiceImpl membershipService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    MembershipMapper membershipMapper;


    @GetMapping // funziona
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllMemberships() {
        return ResponseEntity.ok(membershipMapper.toDtoList(membershipService.findAllMemberships()));
    }

    @GetMapping("/{id}") // Funziona
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMembershipById(@PathVariable Long id) {
        return ResponseEntity.ok(membershipMapper.toDto(membershipService.findMembershipById(id)));
    }

    @PostMapping // Funziona
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveMembership(@RequestBody MembershipRequestDto request) {
        System.out.println("ESUB: " + request.getSubType());
        Membership membership = membershipService.createMembership(userService.findUserById(request.getIdUser()), request.getSubType(), request.getStartDate());
        membershipService.saveMembership(membership);
        return  ResponseEntity.ok(membershipMapper.toDto(membership));
    }

    @DeleteMapping("/{id}") // Funziona
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMembershipById(@PathVariable Long id) {
        membershipService.deleteMembershipById(id);
        return ResponseEntity.ok("Membership deleted successfully");
    }

    @PutMapping("/{id}") // Funziona
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateMembershipById(@PathVariable Long id, @RequestBody MembershipRequestDto request) {
        Membership m =  membershipService.findMembershipById(id);
        m.setUser(userService.findUserById(request.getIdUser()));
        m.setStartDate(request.getStartDate());
        m.setSubType(request.getSubType());
        m.setEndDate();
        membershipService.saveMembership(m);
        return  ResponseEntity.ok(membershipMapper.toDto(m));
    }
}
