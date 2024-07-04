package com.myCompany.gymBro.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "class_type")
@Getter
@Setter
@NoArgsConstructor
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "class_id", nullable = false, updatable = false)
    private UUID classId;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "class_description", columnDefinition = "TEXT")
    private String classDescription;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isActive;

    @OneToMany(mappedBy = "classType", fetch = FetchType.LAZY)
    private List<ScheduleEntity> schedules;

    @OneToMany(mappedBy = "classEntity", fetch = FetchType.LAZY)
    private List<SubscriptionClassEntity> subscriptionClasses;
}
