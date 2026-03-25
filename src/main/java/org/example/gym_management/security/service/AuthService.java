package org.example.gym_management.security.service;

import org.example.gym_management.security.entity.User;
import org.example.gym_management.security.payload.LoginDto;
import org.example.gym_management.security.payload.RegisterDto;

public interface AuthService {
    
	String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
    void createAdmin();
    User makeAdmin(User user);
}
