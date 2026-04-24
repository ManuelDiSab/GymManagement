package org.example.gym_management.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gym_management.security.entity.ERole;
import org.example.gym_management.security.entity.Role;
import org.example.gym_management.security.payload.LoginDto;
import org.example.gym_management.security.payload.RegisterDto;
import org.example.gym_management.security.repository.RoleRepository;
import org.example.gym_management.security.repository.UserRepository;
import org.example.gym_management.security.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthControllerIT {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
//        roleRepository.deleteAll();
//
//        roleRepository.saveAll(List.of(
//                new Role( ERole.ROLE_CLIENT),
//                new Role(, ERole.ROLE_INSTRUCTOR),
//                new Role(, ERole.ROLE_ADMIN)
//        ));
    }

    @Test
    @DisplayName("Post /api/auth/regiter or /api/auth/signup -> Success")
    void RegisterSuccess() throws Exception {
        RegisterDto dto = new RegisterDto();
        dto.setUsername("test");
        dto.setEmail("test@mail");
        dto.setPassword("password");
        dto.setRoles(Set.of("CLIENT"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Post /api/auth/regiter or /api/auth/signup -> Should fail because username already exists (409 conflict)")
    void RegisterUserAlreadyExisting() throws Exception {
        RegisterDto dto = new RegisterDto("Sandro","test", "test@mail", "password", Set.of("CLIENT"));
        authService.register(dto);

        RegisterDto dtoDuplicated = new RegisterDto("Sandro","test", "cvbvd@mail", "password", Set.of("CLIENT"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoDuplicated)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Account with this username already exists!."));
    }

    @Test
    @DisplayName("Post /api/auth/regiter or /api/auth/signup -> Should fail because email already exists (409 conflict)")
    void RegisterEmailAlreadyExisting() throws Exception {
        RegisterDto dto = new RegisterDto("Sandro","test", "test@mail", "password", Set.of("CLIENT"));
        authService.register(dto);

        RegisterDto dtoDuplicated = new RegisterDto("Sandro","testDuplicato", "test@mail", "password", Set.of("CLIENT"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoDuplicated)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Account with this email already exists!."));
    }


    @Test
    @DisplayName("Post /api/auth/regiter or /api/auth/signup -> Should fail (400 bad request)")
    void RegisterMissingEmail() throws Exception {
        RegisterDto dto = new RegisterDto();
        dto.setUsername("test");
        dto.setPassword("password");
        dto.setRoles(Set.of("CLIENT"));
        // It's missing the eamil
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Post /api/auth/login or /api/auth/signin -> Success (200 + JWT)" )
    void LoginSuccess() throws Exception {
        //Registering first
        RegisterDto dto = new RegisterDto();
        dto.setUsername("test");
        dto.setEmail("test@mail");
        dto.setPassword("password");
        dto.setRoles(Set.of("CLIENT"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
        //Login after register
        LoginDto loginDto = new LoginDto();
        loginDto.setPassword("password");
        loginDto.setUsername("test");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    @DisplayName("Login with wrong credential should return 401 anauthorized")
    void LoginWrongCredential() throws Exception {
        RegisterDto dto = new RegisterDto();
        dto.setUsername("test");
        dto.setEmail("test@mail");
        dto.setPassword("password");
        dto.setRoles(Set.of("CLIENT"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
        //Login after register
        LoginDto loginDto = new LoginDto();
        loginDto.setPassword("passeorddd"); // Wrong password
        loginDto.setUsername("test");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Login with empty credential should return 401 anauthorized")
    void LoginEmptyCredential() throws Exception {
        RegisterDto dto = new RegisterDto("Mario", "test", "test@mail", "password", Set.of("CLIENT"));
        authService.register(dto);
        //Login after register
        LoginDto loginDto = new LoginDto("", "");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }



}