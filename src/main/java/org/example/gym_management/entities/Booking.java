package org.example.gym_management.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.example.gym_management.security.entity.User;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "booking_id")
    private long  id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User client;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private GymClass gymClass;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
