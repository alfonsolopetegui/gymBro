package com.myCompany.gymBro.service.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Modelo para la creación de nuevas clases para el gimnasio")
public class ClassCreationDTO {

    @NotBlank(message = "Este campo no puede estar vacío")
    @Schema(description = "Nombre de la clase", example = "Pilates")
    private String className;

    @NotBlank(message = "Este campo no puede estar vacío")
    @Schema(description = "Descripción de la clase", example = "Trabajo de fuerza y elasticidad")
    private String classDescription;
}
