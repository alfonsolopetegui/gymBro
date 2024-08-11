package com.myCompany.gymBro.persistence.entity;

import com.myCompany.gymBro.persistence.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private SubscriptionEntity subscription;

    @Column(name = "last_payment_date")
    private LocalDateTime lastPaymentDate;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean locked;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean disabled;

    //Agregado recientemente
    @Column(name = "create_calendar_events", columnDefinition = "TINYINT(1)")
    private Boolean createCalendarEvents;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PaymentEntity> payments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserRegistrationEntity> registrations;

    //Agregado recientemente
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private GoogleTokenEntity googleToken;

    @Override
    public String toString() {
        return "UserEntity{" +
                "createCalendarEvents=" + createCalendarEvents +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userRole=" + userRole +
                ", subscription=" + subscription.getSubscriptionName() +
                ", lastPaymentDate=" + lastPaymentDate +
                ", locked=" + locked +
                ", disabled=" + disabled +
                ", payments=" + payments +
                ", registrations=" + registrations +
                ", googleToken=" + googleToken +
                '}';
    }
}
