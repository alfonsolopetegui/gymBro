package com.myCompany.gymBro.persistence.entity;

import com.myCompany.gymBro.persistence.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subscription_id", nullable = false, updatable = false)
    private UUID subscriptionId;

    @Column(name = "subscription_name", nullable = false)
    private String subscriptionName;

    @Column(name = "subscription_type")
    @Enumerated(EnumType.STRING)
    private PaymentType subscriptionType;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "subscription", fetch = FetchType.LAZY)
    private List<UserEntity> users;

    @OneToMany(mappedBy = "subscription", fetch = FetchType.LAZY)
    private List<SubscriptionClassEntity> classes;
}
