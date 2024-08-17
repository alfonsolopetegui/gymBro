package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Modelo que representa un horario de clase, de manera resumida")
public class ScheduleSummaryDTO {

    @Schema(description = "Lista de días en que se realiza la clase", example = "[\"MONDAY\", \"WEDNESDAY\"]")
    private List<DayOfWeek> days;

    @Schema(description = "Hora de inicio", type = "string", format = "HH:mm:ss", example = "09:00:00")
    private LocalTime startTime;

    @Schema(description = "Hora de finalización", type = "string", format = "HH:mm:ss", example = "10:00:00")
    private LocalTime endTime;

    public ScheduleSummaryDTO(ScheduleEntity schedule) {
        this.days = schedule.getDays();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
    }
}
