package org.example.gym_management.security.service;


import java.util.HashSet;
import java.util.Set;

import org.example.gym_management.security.entity.ERole;
import org.example.gym_management.security.entity.Role;
import org.example.gym_management.security.entity.User;
import org.example.gym_management.security.exception.MyAPIException;
import org.example.gym_management.security.payload.LoginDto;
import org.example.gym_management.security.payload.RegisterDto;
import org.example.gym_management.security.repository.RoleRepository;
import org.example.gym_management.security.repository.UserRepository;
import org.example.gym_management.security.security.JwtTokenProvider;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired @Qualifier("creaSuperAdmin") ObjectProvider<User> admin;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {
        
    	Authentication authentication = authenticationManager.authenticate(
        		new UsernamePasswordAuthenticationToken(
        				loginDto.getUsername(), loginDto.getPassword()
        		)
        ); 
    	System.out.println(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String register(RegisterDto registerDto) {
        // add check for username exists in database
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new MyAPIException(HttpStatus.CONFLICT, "Account with this username already exists!.");
        }
        // add check for email exists in database
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new MyAPIException(HttpStatus.CONFLICT, "Account with this email already exists!.");
        }
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        // Creo un set vuoto
        Set<Role> roles = new HashSet<>();
        // I valori li assegno in base se passo i ruoli dal client o meno.
        if(registerDto.getRoles() != null) {
	        registerDto.getRoles().forEach(role -> {
	        	Role userRole = roleRepository.findByRoleName(getRole(role)).get();
	        	roles.add(userRole);
	        });
        } else {
        	Role userRole = roleRepository.findByRoleName(ERole.ROLE_CLIENT).get();
        	roles.add(userRole);
        }
        user.setRoles(roles);
        System.out.println(user);
        userRepository.save(user);
        return "User registered successfully!.";
    }

    @Override
    public User makeAdmin(User user) {
        Role admin =  roleRepository.findByRoleName(ERole.ROLE_ADMIN).orElse(null);
        System.out.println("ruolo "  + admin);
        user.getRoles().add(admin);
        return userRepository.save(user);
    }

    @Override
    public void createAdmin() {
        Set<Role> userRoles = new HashSet<>();
        User user  = admin.getObject(userRoles);
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRoles.add(roleRepository.findByRoleName(ERole.ROLE_ADMIN).orElse(null));
        userRoles.add(roleRepository.findByRoleName(ERole.ROLE_CLIENT).orElse(null));
        userRoles.add(roleRepository.findByRoleName(ERole.ROLE_INSTRUCTOR).orElse(null));
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    public ERole getRole(String role) {
    	if(role.equals("ADMIN")) return ERole.ROLE_ADMIN;
    	else if(role.equals("INSTRUCTOR")) return ERole.ROLE_INSTRUCTOR;
    	else return ERole.ROLE_CLIENT;
    }

}
