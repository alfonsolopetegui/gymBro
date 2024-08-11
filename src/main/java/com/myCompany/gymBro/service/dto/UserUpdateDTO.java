package com.myCompany.gymBro.service.dto;


import com.myCompany.gymBro.persistence.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDTO {

    @NotNull(message = "Este campo no puede ser nulo")
    private UUID userId;

    @NotBlank(message = "Este campo no puede ser nulo")
    @Schema(example = "Pablo Mora")
    private String username;

    @Schema(example = "pabloMo333@gmail.com")
    @NotBlank(message = "Este campo no puede estar vacío")
    @Email(message = "Formato incorrecto de email")
    private String email;

    @NotBlank(message = "Este campo no puede estar vacío")
    private String password;

    @Schema(example = "ADMIN")
    @NotNull(message = "Este campo no puede ser nulo")
    private UserRole userRole;

    @NotNull(message = "Este campo no puede ser nulo")
    private UUID subscriptionId;

    @NotNull(message = "Este campo no puede ser nulo")
    private Boolean locked;

    @NotNull(message = "Este campo no puede ser nulo")
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
