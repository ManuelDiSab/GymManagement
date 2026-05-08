package org.example.gym_management.controllers;

import jakarta.validation.Valid;
import org.example.gym_management.dto.GymClassRequestDto;
import org.example.gym_management.entities.GymClass;
import org.example.gym_management.security.entity.User;
import org.example.gym_management.services.GymClassServiceImpl;
import org.example.gym_management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/classes")
public class GymClassController {
    private final GymClassServiceImpl gymClassService;
    private final UserService userService;

    public GymClassController(GymClassServiceImpl gymClassService, UserService userService) {
        this.gymClassService = gymClassService;
        this.userService = userService;
    }


    @GetMapping
    @PreAuthorize("isAuthenticated()") // Funziona
    public ResponseEntity<?> findAllGymClasses() {
        return ResponseEntity.ok(gymClassService.findAllGymClasses());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()") // Funziona
    public ResponseEntity<?> findGymClassById(@PathVariable Long id) {
        return  ResponseEntity.ok(gymClassService.findGymCLassById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")  // Funziona
    public ResponseEntity<?> createGymClass(@RequestBody @Valid GymClassRequestDto request) {
        User instructor = userService.findUserById(request.getInstructorId());
        GymClass g = gymClassService.createCustomGymCLass(request.getName(), request.getDescription(), request.getNPlaces(), request.getStartDate(), request.getEndDate(), instructor);
        return ResponseEntity.ok(gymClassService.saveGymClass(g));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Funziona
    public ResponseEntity<?> deleteGymClass(@PathVariable Long id) {
        gymClassService.deleteGymClass(gymClassService.findGymCLassById(id));
        return ResponseEntity.ok("This gym class has been deleted");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Funziona
    public ResponseEntity<?> updateGymClass(@PathVariable Long id,@Valid @RequestBody GymClassRequestDto request) {
        GymClass g = gymClassService.findGymCLassById(id);
        if (g == null) {
            return new ResponseEntity<>("No Class with that id!", HttpStatus.NOT_FOUND);
        }

        g = gymClassService.updateCustomGymCLass(request, g);
        gymClassService.saveGymClass(g);

        return new ResponseEntity<>("Class updated correctly!", HttpStatus.OK);
    }
}
