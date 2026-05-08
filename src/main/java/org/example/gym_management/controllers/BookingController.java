package org.example.gym_management.controllers;

import jakarta.validation.Valid;
import org.example.gym_management.dto.BookingRequestDto;
import org.example.gym_management.entities.Booking;
import org.example.gym_management.entities.GymClass;
import org.example.gym_management.security.entity.User;
import org.example.gym_management.services.*;
import org.example.gym_management.utilities.BookingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private final BookingServiceImpl bookingService;
    private final UserServiceImpl userService;
    private final GymClassServiceImpl gymClassService;
    private final BookingMapper bookingMapper;

    public BookingController(BookingServiceImpl bookingService, UserServiceImpl userService, GymClassServiceImpl gymClassService, BookingMapper bookingMapper) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.gymClassService = gymClassService;
        this.bookingMapper = bookingMapper;
    }

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
    public ResponseEntity<?> postBooking(@RequestBody @Valid  BookingRequestDto request) {
        User client = userService.findUserById(request.getClientId());
        GymClass gymClass = gymClassService.findGymCLassById(request.getGymClassId());
        Booking booking = bookingService.createBooking(client, gymClass);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.saveBooking(booking));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')") // Funziona
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        bookingService.deleteBooking(bookingService.findBookingById(id));
        return ResponseEntity.ok("Booking has been deleted");
    }
}
