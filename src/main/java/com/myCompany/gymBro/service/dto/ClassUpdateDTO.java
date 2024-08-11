package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.ClassEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ClassUpdateDTO {

    @NotNull(message = "este campo no puede ser nulo")
    @Schema(example = "c71c6e80-c113-4568-b938-93e69febbcbd")
    private UUID classId;

    @NotEmpty(message = "este campo no puede estar vacío")
    @NotBlank(message = "Este campo no puede estar vacío")
    @Schema(example = "Pilates")
    private String className;

    @NotEmpty(message = "este campo no puede estar vacío")
    @NotBlank(message = "Este campo no puede estar vacío")
    @Schema(example = "Fuerza y elongación")
    private String classDescription;

    @NotNull(message = "este campo no puede ser nulo")
    private Boolean isActive;

    public ClassUpdateDTO(ClassEntity classEntity) {
        this.classDescription = classEntity.getClassDescription();
        this.classId = classEntity.getClassId();
        this.className = classEntity.getClassName();
        this.isActive = classEntity.getIsActive();
    }
}
