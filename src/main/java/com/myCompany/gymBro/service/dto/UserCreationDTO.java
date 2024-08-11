package com.myCompany.gymBro.service.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class UserCreationDTO {

    //DTO usado para la creación de usuarios. Con los datos requeridos

    @NotBlank(message = "Este campo no puede estar vacío")
    private String username;

    @NotBlank(message = "Este campo no puede estar vacío")
    @Email
    private String email;

    @NotBlank(message = "Este campo no puede estar vacío")
    private String password;

    @NotNull(message = "Este campo no puede estar vacío")
    private UUID subscriptionId;

}
