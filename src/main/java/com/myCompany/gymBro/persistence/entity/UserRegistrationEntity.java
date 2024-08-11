package com.myCompany.gymBro.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_registration")
@Getter
@Setter
@NoArgsConstructor
public class UserRegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "registration_id", nullable = false, updatable = false)
    private UUID registrationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Override
    public String toString() {
        return "UserRegistrationEntity{" +
                "registrationDate=" + registrationDate +
                ", registrationId=" + registrationId +
                ", user=" + user.getUserId() +
                ", schedule=" + schedule.getScheduleId() +
                '}';
    }
}
