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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc


class   BookingControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private GymClassRepository gymClassRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private MembershipRepository  membershipRepository;
    @Autowired private WebApplicationContext webApplicationContext;

    private Long clientID;
    private Long gymClassID;



    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity()) // This is needed to make @WithMockUSer work
                .build();

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
    @WithMockUser(username = "Test_admin", roles = {"ADMIN"})
    @DisplayName("GET /api/booking - Admin should get all bookings")
    void findAllBookings() throws Exception {
        // Create a booking
        String bookingJson = String.format("{\"clientId\": %d, \"gymClassId\": %d}", clientID, gymClassID);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/booking")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/booking")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "Test_client", roles = {"CLIENT"})
    @DisplayName("GET /api/booking - Client should be forbidden")
    void findAllBookingsForbiddenForClient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/booking")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Test_client", roles = {"CLIENT"})
    @DisplayName("GET /api/booking/user/{id} - Client should see own bookings")
    void findBookingByUser() throws Exception {
        // Create a booking
        String bookingJson = String.format("{\"clientId\": %d, \"gymClassId\": %d}", clientID, gymClassID);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/booking")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(status().isCreated());

        // Then search user's bookings
        mockMvc.perform(MockMvcRequestBuilders.get("/api/booking/user/" + clientID)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "Test_client", roles = {"CLIENT"})
    @DisplayName("POST /api/booking - Should return 409 when booking same class twice")
    void createBookingAlreadyBooked() throws Exception {
        String bookingJson = String.format("{\"clientId\": %d, \"gymClassId\": %d}", clientID, gymClassID);
        // first booking should be created correctly
        mockMvc.perform(MockMvcRequestBuilders.post("/api/booking")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(status().isCreated());

        // second booking should fail
        mockMvc.perform(MockMvcRequestBuilders.post("/api/booking")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "Test_admin", roles = {"ADMIN"})
    @DisplayName("DELETE /api/booking/{id} - Admin should delete booking")
    void cancelBooking() throws Exception {
        String bookingJson = String.format("{\"clientId\": %d, \"gymClassId\": %d}", clientID, gymClassID);
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/booking")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Take the id
        long bookingId = new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(response).get("id").asLong();

        // Delete
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/booking/" + bookingId)
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}