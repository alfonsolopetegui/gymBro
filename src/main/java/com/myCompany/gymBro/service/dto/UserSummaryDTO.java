package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserSummaryDTO {

    //DTO usado como respuesta resumida

    private UUID userId;
    private String username;
    private String email;
    private String subscriptionName;

    // Constructor que toma un UserEntity
    public UserSummaryDTO(UserEntity user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.subscriptionName = user.getSubscription().getSubscriptionName();
    }

}
