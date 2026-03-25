package org.example.gym_management.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gym_management.security.entity.User;

import java.time.LocalDate;
@NoArgsConstructor
@Data
@Entity
@Table(name = "subscriptions")
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_id", nullable = false)
    private Long id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EMembership subType;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
//    @Column(nullable = false)
//    private double  price;

    public Membership(EMembership subType, User user) {
        this.subType = subType;
        this.startDate = LocalDate.now();
        this.user = user;
        this.endDate = calculateEndDate(subType, LocalDate.now());
    }

    private LocalDate calculateEndDate(EMembership subType, LocalDate startDate) {
        return switch (subType) {
            case SUB_ANNUAL -> startDate.plusYears(1);
            case SUB_MONTHLY -> startDate.plusMonths(1);
            case SUB_QUARTERLY -> startDate.plusDays(3);
        };
    }

    public void setEndDate(){
        this.endDate = calculateEndDate(subType, startDate);
    }

//    private double calculatePrice(EMembership subType) {
//        return switch (subType) {
//            case SUB_ANNUAL -> 549.99;
//            case SUB_MONTHLY -> 49.90;
//            case SUB_QUARTERLY -> 145.99;
//        };
//    }

}
