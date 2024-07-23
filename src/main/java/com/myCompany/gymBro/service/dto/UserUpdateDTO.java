package com.myCompany.gymBro.service.dto;


import com.myCompany.gymBro.persistence.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDTO {

    private String userId;
    private String username;
    private String email;
    private String password;
    private UserRole userRole;
    private String subscriptionId;
    private Boolean locked;
    private Boolean disabled;

    public UserUpdateDTO(UserUpdateDTO user) {
        this.disabled = user.getDisabled();
        this.email = user.getEmail();
        this.locked = user.getLocked();
        this.password = user.getPassword();
        this.subscriptionId = user.getSubscriptionId();
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.userRole = user.getUserRole();
    }
}
