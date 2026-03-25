package org.example.gym_management.security.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDto {
    private String username;
    private String password;
}

//Il client dovrà inviare un oggetto JSON nel body con questa forma
/*{
    "username": "mariorossi",
    "password": "Pa$$w0rd!"
}*/
