package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.UserEntity;
import com.myCompany.gymBro.persistence.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailsDTO {

    private UUID userId;
    private String username;
    private String email;
    private String subscriptionName;
    private Double subscriptionPrice;
    private UserRole userRole;
    private Boolean locked;
    private Boolean disabled;

    public UserDetailsDTO(UserEntity user) {
        this.disabled = user.getDisabled();
        this.email = user.getEmail();
        this.locked = user.getLocked();
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.userRole = user.getUserRole();
        if (user.getSubscription() != null) {

            this.subscriptionName = user.getSubscription().getSubscriptionName();
            this.subscriptionPrice = user.getSubscription().getPrice();

        } else {
            this.subscriptionName = null;
            this.subscriptionPrice = null;
        }
    }


}
