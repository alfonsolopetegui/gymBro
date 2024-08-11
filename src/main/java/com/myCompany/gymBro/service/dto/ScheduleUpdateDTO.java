package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleUpdateDTO {

    @NotNull(message = "Este campo no puede ser nulo")
    private UUID scheduleId;

    @NotNull(message = "Este campo no puede ser nulo")
    private UUID classId;

    @NotNull(message = "Este campo no puede ser nulo")
    @Schema(description = "Hora de inicio", type = "string", format = "HH:mm:ss", example = "09:00:00")
    private LocalTime startTime;

    @NotNull(message = "Este campo no puede ser nulo")
    @Schema(description = "Hora de inicio", type = "string", format = "HH:mm:ss", example = "10:00:00")
    private LocalTime endTime;

    @NotEmpty(message = "Este campo no puede estar vacío")
    private List<String> days;

    @Min(value = 1, message = "El número máximo de registros debe ser al menos 1")
    private int maxRegistrations;

    public ScheduleUpdateDTO(ScheduleEntity schedule) {
        this.scheduleId = schedule.getScheduleId();
        this.classId = schedule.getClassType().getClassId();
        this.days = schedule.getDays().stream().map(day -> day.getDay().toString()).collect(Collectors.toList());
        this.endTime = schedule.getEndTime();
        this.maxRegistrations = schedule.getMaxRegistrations();
        this.startTime = schedule.getStartTime();
    }
}
