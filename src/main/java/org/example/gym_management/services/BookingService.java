package org.example.gym_management.services;

import org.example.gym_management.entities.Booking;
import org.example.gym_management.entities.GymClass;
import org.example.gym_management.security.entity.User;

import java.util.List;

public interface BookingService {
    public List<Booking> findByGymClass(GymClass gymClass);
    public Booking findBookingById(long id);
    public Booking saveBooking(Booking booking);
    public void deleteBooking(Booking booking);
    public List<Booking> findAllBooking();
    public List<Booking> findAllBookingByUser(User user);

}
