package org.example.gym_management.security.configuration;

import org.example.gym_management.security.entity.Role;
import org.example.gym_management.security.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class AdminConfiguration {


    @Value("${user.admin.username}") private String username;
    @Value("${user.admin.password}") private String password;
    @Value("${user.admin.email}") private String email;

    @Bean
    @Scope("singleton")
    public User creaSuperAdmin(Set<Role> roles) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        user.setBookings(new HashSet<>());
        return user;
    }
}
