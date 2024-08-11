package com.myCompany.gymBro.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RegistrationCreationDTO {

    @NotNull(message = "Este campo no puede estar vacío")
    private UUID userId;

    @NotNull(message = "Este campo no puede estar vacío")
    private UUID scheduleId;

    @Override
    public String toString() {
        return "RegistrationCreationDTO{" +
                "scheduleId='" + scheduleId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
