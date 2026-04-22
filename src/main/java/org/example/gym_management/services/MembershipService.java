package org.example.gym_management.services;

import org.example.gym_management.entities.GymClass;
import org.example.gym_management.entities.Membership;

import java.util.List;

public interface MembershipService {
    public Membership saveMembership(Membership membership);
    public Membership findMembershipById(Long id);
    public void deleteMembershipById(Long id);
    public List<Membership> findAllMemberships();
    public void updateExpiredMembership();
}
