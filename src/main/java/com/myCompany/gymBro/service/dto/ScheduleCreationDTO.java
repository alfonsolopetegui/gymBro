package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleCreationDTO {

    @NotNull(message = "este campo no puede ser nulo")
    private UUID classId;

    @NotNull(message = "este campo no puede ser nulo")
    @Schema(description = "Hora de inicio", type = "string", format = "HH:mm:ss", example = "09:00:00")
    private LocalTime startTime;

    @NotNull(message = "este campo no puede ser nulo")
    @Schema(description = "Hora de inicio", type = "string", format = "HH:mm:ss", example = "10:00:00")
    private LocalTime endTime;

    @NotEmpty(message = "este campo no puede estar vacío")
    private List<DayOfWeek> days;

    @Min(value = 1, message = "El número máximo de registros debe ser al menos 1")
    private int maxRegistrations;

    public ScheduleCreationDTO(ScheduleEntity schedule) {
        this.classId = schedule.getClassType().getClassId();
        this.days = schedule.getDays();
        this.endTime = schedule.getEndTime();
        this.maxRegistrations = schedule.getMaxRegistrations();
        this.startTime = schedule.getStartTime();
    }
}
