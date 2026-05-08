package org.example.gym_management.security.service;


import java.util.HashSet;
import java.util.Set;

import org.example.gym_management.security.entity.ERole;
import org.example.gym_management.security.entity.Role;
import org.example.gym_management.security.entity.User;
import org.example.gym_management.security.exception.MyAPIException;
import org.example.gym_management.security.payload.LoginDto;
import org.example.gym_management.security.payload.RegisterDto;
import org.example.gym_management.security.repository.RoleRepository;
import org.example.gym_management.security.repository.UserRepository;
import org.example.gym_management.security.security.JwtTokenProvider;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final ObjectProvider<User> admin;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           @Qualifier("creaSuperAdmin") ObjectProvider<User> admin) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.admin = admin;
    }

    @Override
    public String login(LoginDto loginDto) {
        
    	Authentication authentication = authenticationManager.authenticate(
        		new UsernamePasswordAuthenticationToken(
        				loginDto.getUsername(), loginDto.getPassword()
        		)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String register(RegisterDto registerDto) {
        // add check for username exists in database
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new MyAPIException(HttpStatus.CONFLICT, "Account with this username already exists!.");
        }
        // add check for email exists in database
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new MyAPIException(HttpStatus.CONFLICT, "Account with this email already exists!.");
        }
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        // IT: Creo un set vuoto |EN: I create an empty set
        Set<Role> roles = new HashSet<>();
        // IT: I valori li assegno in base se passo i ruoli dal client o meno.
        // EN: I assign the values based on whether I pass the roles from the client or not.
        if(registerDto.getRoles() != null) {
            registerDto.getRoles().forEach(role -> {
                ERole eRole = getRole(role);
                /* IT: Blocco Privilege Escalation: il ruolo ADMIN non può essere
                 * auto-assegnato durante la registrazione pubblica.
                 * Per promuovere un utente ad ADMIN, usare l'endpoint dedicato:
                 * PATCH /api/users/{id}/makeadmin  (solo Admin può chiamarlo)
                 * EN: Privilege Escalation Block: The ADMIN role cannot be
                 * self-assigned during public registration.
                 * To promote a user to ADMIN, use the dedicated endpoint:
                 * PATCH /api/users/{id}/makeadmin (only Admin can call it) */
                if(eRole == ERole.ROLE_ADMIN) {
                    throw new MyAPIException(HttpStatus.FORBIDDEN,
                        "Non puoi assegnarti il ruolo ADMIN in fase di registrazione.");
                }
                Role userRole = roleRepository.findByRoleName(eRole)
                    .orElseThrow(() -> new MyAPIException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Ruolo non trovato nel database: " + role));
                roles.add(userRole);
            });
        } else {
            Role userRole = roleRepository.findByRoleName(ERole.ROLE_CLIENT)
                .orElseThrow(() -> new MyAPIException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ruolo CLIENT non trovato nel database."));
            roles.add(userRole);
        }
        user.setRoles(roles);
        userRepository.save(user);
        return "User registered successfully!.";
    }

    @Override
    public User makeAdmin(User user) {
        Role roleAdmin =  roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new MyAPIException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Admin role not found in database."));
        user.getRoles().add(roleAdmin);
        return userRepository.save(user);
    }

    @Override
    public void createAdmin() {
        Set<Role> userRoles = new HashSet<>();
        User user  = admin.getObject(userRoles);

        //IT: Se l'admin esiste non faccio niente | EN: If admin already exist do nothing
        if(userRepository.existsByUsername(user.getUsername())) return;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRoles.add(roleRepository.findByRoleName(ERole.ROLE_ADMIN).orElse(null));
        userRoles.add(roleRepository.findByRoleName(ERole.ROLE_CLIENT).orElse(null));
        userRoles.add(roleRepository.findByRoleName(ERole.ROLE_INSTRUCTOR).orElse(null));
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    public ERole getRole(String role) {
    	try{
            return ERole.valueOf("ROLE_" + role.toUpperCase());
        }catch(IllegalArgumentException e){
            throw new MyAPIException(HttpStatus.BAD_REQUEST, "Invalid role : " + role);
        }
    }

}
