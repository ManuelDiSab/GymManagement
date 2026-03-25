package org.example.gym_management.controllers;

import org.example.gym_management.dto.BookingRequestDto;
import org.example.gym_management.entities.Booking;
import org.example.gym_management.entities.GymClass;
import org.example.gym_management.security.entity.User;
import org.example.gym_management.services.*;
import org.example.gym_management.utilities.BookingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    @Autowired
    BookingServiceImpl bookingService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    GymClassServiceImpl gymClassService;
    @Autowired
    BookingMapper bookingMapper;

    @GetMapping // Funziona
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<?> findAllBookings() {
        List<Booking> b = bookingService.findAllBooking();
        return ResponseEntity.ok(bookingMapper.toDtoList(b));
    }


    @GetMapping("/user/{id}") // Funziona
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> findBookingByUser(@PathVariable Long id) {
        List<Booking> lista = bookingService.findAllBookingByUser(userService.findUserById(id));
        return ResponseEntity.ok(bookingMapper.toDtoList(lista));
    }


    @PostMapping
    @PreAuthorize("isAuthenticated()") // Funziona
    public ResponseEntity<?> postBooking(@RequestBody BookingRequestDto request) {
        User client = userService.findUserById(request.getClientId());
        GymClass gymClass = gymClassService.findGymCLassById(request.getGymClassId());
        Booking booking = bookingService.createBooking(client, gymClass);
        return ResponseEntity.ok(bookingService.saveBooking(booking));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')") // Funziona
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        bookingService.deleteBooking(bookingService.findBookingById(id));
        return ResponseEntity.ok("Booking has been deleted");
    }
}
