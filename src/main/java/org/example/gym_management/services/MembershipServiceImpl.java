package org.example.gym_management.services;

import org.example.gym_management.entities.EMembership;
import org.example.gym_management.entities.Membership;
import org.example.gym_management.reposiotries.MembershipRepository;
import org.example.gym_management.security.entity.User;
import org.example.gym_management.security.exception.ClientException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MembershipServiceImpl implements MembershipService {
    @Autowired
    MembershipRepository membershipRepository;
    @Autowired @Qualifier("createMembership") ObjectProvider<Membership> membershipProvider;

    public Membership createMembership(User user, EMembership subType, LocalDate startDate) {
        if(!user.isClient()){
            throw new ClientException("The user has to be a client to create a membership");
        }
        Membership m = membershipProvider.getObject();
        m.setUser(user);
        m.setSubType(subType);
        m.setStartDate(startDate);
        m.setEndDate();
        return m;
    }

    //Database methods
    @Override
    public Membership saveMembership(Membership membership) {
        return membershipRepository.save(membership);
    }

    @Override
    public Membership findMembershipById(Long id) {
        return membershipRepository.findById(id).get();
    }

    @Override
    public void deleteMembershipById(Long id) {
        membershipRepository.deleteById(id);
    }

    @Override
    public List<Membership> findAllMemberships() {
        return membershipRepository.findAll();
    }


}
