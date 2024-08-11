package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.UserEntity;
import com.myCompany.gymBro.persistence.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailsDTO {

    @NotNull(message = "Este campo no puede ser null")
    private UUID userId;

    @NotBlank(message = "Este campo no puede estar vacío")
    private String username;

    @NotBlank(message = "Este campo no puede estar vacío")
    @Email(message = "Formato incorrecto de email")
    private String email;

    @NotBlank(message = "Este campo no puede estar vacío")
    private String subscriptionName;

    @NotNull(message = "Este campo no puede ser null")
    @Positive(message = "el precio debe ser un valor positivo")
    private Double subscriptionPrice;

    @NotNull(message = "Este campo no puede ser null")
    private UserRole userRole;

    @NotNull(message = "Este campo no puede ser null")
    private Boolean locked;

    @NotNull(message = "Este campo no puede ser null")
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
