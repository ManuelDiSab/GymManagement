package org.example.gym_management.services;

import org.example.gym_management.entities.Booking;
import org.example.gym_management.entities.GymClass;
import org.example.gym_management.reposiotries.BookingRepository;
import org.example.gym_management.security.entity.User;
import org.example.gym_management.security.exception.ClassIsFullException;
import org.example.gym_management.security.exception.ClientException;
import org.example.gym_management.security.exception.UserIsBusyException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired @Qualifier("createBooking")
    ObjectProvider<Booking> bookingProvider;

    public Booking createBooking(User client, GymClass gymClass) {
        if(!client.isClient()){
            throw new ClientException("Only a client can book a class");
        }
        if(!client.getSubscription().isActive()){
            throw new ClientException("User membership is not active");
        }
        if (bookingRepository.countByGymClassId(gymClass.getId()) >= gymClass.getNPlaces()) {
            throw new ClassIsFullException("This class is already full");
        }
        if (bookingRepository.existsByClientIdAndGymClassId(client.getId(), gymClass.getId())) {
            throw new ClientException("Already booked this class");
        }
        if (bookingRepository.clientIsBusy(client, gymClass.getStartDate(), gymClass.getEndDate())) {
            throw new UserIsBusyException("Client already has booked a Gym class in that time");
        }

        Booking booking = bookingProvider.getObject();
        booking.setCreatedAt(LocalDateTime.now());
        booking.setGymClass(gymClass);
        booking.setClient(client);
        return booking;
    }

    // Database methods
    @Override
    public List<Booking> findByGymClass(GymClass gymClass) {
        return bookingRepository.findByGymClass(gymClass);
    }



    @Override
    public Booking findBookingById(long id) {
        return bookingRepository.findById(id).get();
    }

    @Override
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Booking booking) {
        bookingRepository.delete(booking);
    }

    @Override
    public List<Booking> findAllBooking() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> findAllBookingByUser(User user) {
        return bookingRepository.findAllByClient(user);
    }
}
