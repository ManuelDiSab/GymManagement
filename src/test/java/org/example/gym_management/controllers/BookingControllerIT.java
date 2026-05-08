package org.example.gym_management.controllers;

import org.example.gym_management.entities.EMembership;
import org.example.gym_management.entities.GymClass;
import org.example.gym_management.entities.Membership;
import org.example.gym_management.repositories.BookingRepository;
import org.example.gym_management.repositories.GymClassRepository;
import org.example.gym_management.repositories.MembershipRepository;
import org.example.gym_management.security.entity.ERole;
import org.example.gym_management.security.entity.Role;
import org.example.gym_management.security.entity.User;
import org.example.gym_management.security.repository.RoleRepository;
import org.example.gym_management.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class BookingControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private GymClassRepository gymClassRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private MembershipRepository  membershipRepository;

    private Long clientID;
    private Long gymClassID;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        gymClassRepository.deleteAll();
        membershipRepository.deleteAll();
        userRepository.deleteAll();
        //Roles
        Role clientRole = roleRepository.findByRoleName(ERole.ROLE_CLIENT).orElseGet(
                () -> roleRepository.save(new Role(null, ERole.ROLE_CLIENT))
        );

        Role instructorRole = roleRepository.findByRoleName(ERole.ROLE_INSTRUCTOR).orElseGet(
                () -> roleRepository.save(new Role(null, ERole.ROLE_INSTRUCTOR))
        );
        // Client
        User client = new User();
        client.setUsername("Test_client");
        client.setEmail("email_test");
        client.setPassword("Test_password");
        client.setRoles(Set.of(clientRole));

        // Instructor
        User instructor = new User();
        instructor.setEmail("email_test_instructor");
        instructor.setUsername("Test instructor");
        instructor.setPassword("Test_password_instructor");
        instructor.setRoles(Set.of(instructorRole));
        userRepository.save(instructor);
        // Membership
        Membership membership = new Membership();
        membership.setActive(true);
        membership.setStartDate(LocalDate.now());
        membership.setUser(client);
        membership.setSubType(EMembership.SUB_MONTHLY); // Before the setEndDate
        membership.setEndDate();
        clientID = userRepository.save(client).getId();
        membershipRepository.save(membership);
        client.setMembership(membership);
        userRepository.save(client);

        // GymClass
        GymClass gymClass = new GymClass();
        gymClass.setName("Gym Class test");
        gymClass.setStartDate(LocalDateTime.now().plusDays(1));
        gymClass.setEndDate(LocalDateTime.now().plusDays(2));
        gymClass.setInstructor(instructor);
        gymClass.setNPlaces(20);
        gymClassID = gymClassRepository.save(gymClass).getId();

    }

    @Test
    @WithMockUser(username = "Test_client", roles = {"CLIENT"})
    @DisplayName("POST /api/booking - Should create booking and return 201")
    void createBookingIntegrationTest() throws Exception {
        String bookingJson = String.format("{\"clientId\": %d, \"gymClassId\": %d}", clientID, gymClassID);
        // WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/booking")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(status().isCreated()) // Should return 201
                .andExpect(jsonPath("$.id").exists()) // Verify that the JSON has an id
                .andExpect(jsonPath("$.client.id").value(clientID));
    }

    @Test
    void findAllBookings() {
    }

    @Test
    void findBookingByUser() {
    }

    @Test
    void postBooking() {
    }

    @Test
    void cancelBooking() {
    }
}