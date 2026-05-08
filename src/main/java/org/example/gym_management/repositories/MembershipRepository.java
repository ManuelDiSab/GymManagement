package org.example.gym_management.repositories;

import jakarta.transaction.Transactional;
import org.example.gym_management.entities.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MembershipRepository extends JpaRepository<Membership,Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Membership m SET m.active = false WHERE m.endDate < :today AND m.active = true")
    void deactivateMembership(@Param("today")LocalDate today);
}
