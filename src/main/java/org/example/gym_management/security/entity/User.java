package org.example.gym_management.security.entity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.gym_management.entities.Booking;
import org.example.gym_management.entities.Membership;

import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") })
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    
    // Il caricamento EAGER delle raccolte significa che vengono recuperate 
    // completamente nel momento in cui viene recuperato il loro genitore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // relazione con abbonamenti
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private Membership subscription;

    //relazione con prenotazioni
    @OneToMany(mappedBy = "client", cascade =  CascadeType.ALL)
    private Set<Booking>  bookings;

    public boolean hasRole(ERole roleName) {
        return getRoles().stream()
                .anyMatch(role -> role.getRoleName() == roleName);
    }

    public boolean isAdmin() {
        return hasRole(ERole.ROLE_ADMIN);
    }

    public boolean isInstructor() {
        return hasRole(ERole.ROLE_INSTRUCTOR);
    }

    public boolean isClient() {
        return hasRole(ERole.ROLE_CLIENT);
    }


}
