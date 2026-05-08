package org.example.gym_management.reposiotries;

import org.example.gym_management.entities.GymClass;
import org.example.gym_management.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface GymClassRepository extends JpaRepository<GymClass, Long> {

    @Query("SELECT (COUNT (g) > 0) FROM GymClass g WHERE g.instructor = :instructor AND  :start < g.endDate AND :end > g.startDate ")
    boolean instructorIsBusy(@Param("instructor") User instructor, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
}
