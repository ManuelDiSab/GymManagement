package org.example.gym_management.config;

import org.example.gym_management.entities.Membership;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class MembershipConfig {

    @Bean
    @Scope("prototype")
    public Membership createMembership(){
        return new Membership();
    }

}
