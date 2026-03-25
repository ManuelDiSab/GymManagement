package org.example.gym_management.config;

import org.example.gym_management.entities.GymClass;
import org.example.gym_management.security.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

@Configuration
public class GymClassConfig {

    @Bean
    @Scope("prototype")
    public GymClass createGymClass() {
        return new GymClass();
    }

    public GymClass createCustomGymCLass(String name, String description, int nPlaces, LocalDateTime start, LocalDateTime end, User instructor) {
        GymClass gymClass = new GymClass();
        gymClass.setName(name);
        return  gymClass;
    }
}
