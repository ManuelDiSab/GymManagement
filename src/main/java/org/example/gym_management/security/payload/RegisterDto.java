package org.example.gym_management.security.payload;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotBlank(message = "Name is mandatory ")
    @Size(min = 2, max = 20, message = "The name should be between 2 and 20 character long")
    private String name;

    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, max = 15, message = "The username should be between 4 and 15 character long")
    private String username;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email format is invalid")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password should be at least 6 character long")
    private String password;
    // Passagio di ruoli dal client (Facoltativo)
    private Set<String> roles;
}

// Il client dovrà inviare un oggetto JSON nel body con questa forma | Client should send a JSON object that looks like this
/*{
    "name": "Francesca Neri",
    "username": "francescaneri",
    "email": "f.neri@example.com",
    "password": "qwerty",
    "roles": ["INSTRUCTOR", "CLIENT"] // Facoltativo
}*/
