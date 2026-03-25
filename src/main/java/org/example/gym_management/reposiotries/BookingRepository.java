package org.example.gym_management.reposiotries;

import org.example.gym_management.entities.Booking;
import org.example.gym_management.entities.GymClass;
import org.example.gym_management.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByGymClass(GymClass gymClass);
    List<Booking> findAllByClient(User user);
    long countByGymClassId(long gymClassId);
    boolean existsByClientIdAndGymClassId(long clientId, long gymClassId);

    @Query("SELECT (COUNT(b) > 0) FROM Booking b JOIN b.gymClass g WHERE b.client = :client AND :start < g.endDate AND :end > g.startDate")
    boolean clientIsBusy( @Param("client") User client, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end
    );


}
