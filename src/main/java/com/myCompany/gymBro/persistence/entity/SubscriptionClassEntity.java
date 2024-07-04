package com.myCompany.gymBro.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "subscription_class")
@Getter
@Setter
@NoArgsConstructor
public class SubscriptionClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subscription_class_id", nullable = false, updatable = false)
    private UUID subscriptionClassId;

    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private SubscriptionEntity subscription;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classEntity;
}
