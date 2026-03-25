package org.example.gym_management.security.repository;

import org.example.gym_management.security.entity.ERole;
import org.example.gym_management.security.entity.Role;
import org.example.gym_management.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

//    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
//    List<User> findByRole(@Param("role") Role role);

    List<User> findByRolesContaining(Role role);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = :role")
    List<User> findAllClients(ERole role);
}
