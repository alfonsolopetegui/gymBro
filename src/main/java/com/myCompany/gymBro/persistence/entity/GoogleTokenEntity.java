package com.myCompany.gymBro.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "google_tokens")
@Getter
@Setter
public class GoogleTokenEntity {


        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "token_id", nullable = false, updatable = false)
        private UUID tokenId;

        @Column(name = "access_token", nullable = false)
        private String accessToken;

        @Column(name = "expires_in", nullable = false)
        private LocalDateTime expiresIn;

        @Column(name = "refresh_token")
        private String refreshToken;

        @OneToOne
        @JoinColumn(name = "user_id", referencedColumnName = "user_id")
        private UserEntity user;

}
