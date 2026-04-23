package org.example.gym_management.services;

import org.example.gym_management.entities.Booking;
import org.example.gym_management.entities.EMembership;
import org.example.gym_management.entities.GymClass;
import org.example.gym_management.entities.Membership;
import org.example.gym_management.reposiotries.BookingRepository;
import org.example.gym_management.security.entity.ERole;
import org.example.gym_management.security.entity.Role;
import org.example.gym_management.security.entity.User;
import org.example.gym_management.security.exception.ClassIsFullException;
import org.example.gym_management.security.exception.ClientException;
import org.example.gym_management.security.exception.UserIsBusyException;
import org.example.gym_management.security.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ObjectProvider<Booking> bookingObjectProvider;
    @InjectMocks
    private BookingServiceImpl bookingService;

    // Oggetti dummy per i test
    private User client;
    private User instructor;
    private GymClass gymClass;

    @BeforeEach
    void setUp() {
        Role clientRole = new Role(1L, ERole.ROLE_CLIENT);
        Role instructorRole = new Role(2L, ERole.ROLE_INSTRUCTOR);


        client = new User();
        client.setId(1L);
        client.setUsername("Test client");
        client.setPassword("Test_password");
        client.setRoles(Set.of(clientRole));

        instructor = new User();
        instructor.setId(2L);
        instructor.setUsername("Test instructor");
        instructor.setPassword("Test_password_instructor");
        instructor.setRoles(Set.of(instructorRole));

        Membership membership = new Membership();
        membership.setId(1L);
        membership.setActive(true);
        membership.setStartDate(LocalDate.now());
        membership.setUser(client);
        membership.setSubType(EMembership.SUB_MONTHLY);
        membership.setEndDate();
        client.setSubscription(membership);

        gymClass = new GymClass();
        gymClass.setId(1L);
        gymClass.setName("Gym Class test");
        gymClass.setStartDate(LocalDateTime.now().plusDays(1));
        gymClass.setEndDate(LocalDateTime.now().plusDays(2));
        gymClass.setInstructor(instructor);
        gymClass.setNPlaces(20);
    }


    @Test
    @DisplayName("Should create booking successfully when all condition are met")
    void shouldCreateBookingSuccessfully() {
        when(bookingRepository.countByGymClassId(anyLong())).thenReturn(10L);
        when(bookingRepository.existsByClientIdAndGymClassId(anyLong(), anyLong())).thenReturn(false);
        when(bookingRepository.clientIsBusy(any(), any(), any())).thenReturn(false);
        when(bookingObjectProvider.getObject()).thenReturn(new  Booking());

        Booking result = bookingService.createBooking(client, gymClass);

        assertNotNull(result);
        assertEquals(client,  result.getClient());
        assertEquals(gymClass,  result.getGymClass());
        verify(bookingRepository, times(1)).countByGymClassId(anyLong());
    }

    @Test
    @DisplayName("Should throw exception for a inactive membership")
    void createBookingWhenInactiveSubscription(){
        client.getSubscription().setActive(false);
        ClientException exception = assertThrows(ClientException.class, () -> bookingService.createBooking(client, gymClass));
        assertEquals("User membership is not active", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw ClassIsFullException")
    void createBookingWhenClassIsFull(){
        when(bookingRepository.countByGymClassId(anyLong())).thenReturn(20L); // No seats left
        ClassIsFullException exception = assertThrows(ClassIsFullException.class, () -> bookingService.createBooking(client, gymClass));
        assertEquals("This class is already full", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw ClientException for booking a gym class twice for the same client")
    void createBookingWhenClassAlreadyBooked(){
        when(bookingRepository.existsByClientIdAndGymClassId(anyLong(), anyLong())).thenReturn(true);
        ClientException exception = assertThrows(ClientException.class, () -> bookingService.createBooking(client, gymClass));
        assertEquals("Already booked this class", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw ClientException when a different class is already booked in the same time")
    void createBookingWhenThereIsAlreadyAClassInTheSameTime(){
        when(bookingRepository.clientIsBusy(any(), any(), any())).thenReturn(true);
        UserIsBusyException exception = assertThrows(UserIsBusyException.class, () -> bookingService.createBooking(client, gymClass));
        assertEquals("Client already has booked a Gym class in that time", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw ClientException when a user that it's not a client try book a class")
    void createBookingWhenUserNotAClient(){
        Membership m = new Membership(EMembership.SUB_QUARTERLY, instructor);
        instructor.setSubscription(m);
        ClientException exception = assertThrows(ClientException.class, () -> bookingService.createBooking(instructor, gymClass));
        assertEquals("Only a client can book a class", exception.getMessage());
    }

}