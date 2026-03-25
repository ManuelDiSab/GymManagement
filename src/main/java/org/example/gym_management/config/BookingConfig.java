package org.example.gym_management.config;

import org.example.gym_management.entities.Booking;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BookingConfig {
    @Bean
    @Scope("prototype")
    public Booking createBooking() {
        return new Booking();
    }
}
