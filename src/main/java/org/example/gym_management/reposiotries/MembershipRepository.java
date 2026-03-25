package org.example.gym_management.reposiotries;

import org.example.gym_management.entities.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<Membership,Long> {
}
