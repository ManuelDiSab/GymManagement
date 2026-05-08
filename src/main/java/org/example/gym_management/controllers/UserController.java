package org.example.gym_management.controllers;


import org.example.gym_management.security.entity.User;
import org.example.gym_management.security.service.AuthService;
import org.example.gym_management.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Funziona
    public ResponseEntity<?> getAllClients() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}") // Funziona
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PatchMapping("/{id}/makeadmin") // Funziona
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> makeAdmin(@PathVariable Long id) {
        User user = userService.findUserById(id);
        authService.makeAdmin(user);
        return ResponseEntity.ok("User " + user.getUsername() + " upgraded to Admin");

    }

    @DeleteMapping("/{id}") // Funziona
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        User user = userService.findUserById(id);
        userService.deleteUser(user);
        return ResponseEntity.ok("User " + user.getUsername() + " deleted");
    }
}

