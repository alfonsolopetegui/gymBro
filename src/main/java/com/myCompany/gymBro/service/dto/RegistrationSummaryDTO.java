package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.UserRegistrationEntity;
import com.myCompany.gymBro.persistence.enums.DayOfWeek;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationSummaryDTO {


    private String userName;

    private String className;

    private List<DayOfWeek> days;

    @Schema(description = "Hora de inicio", type = "string", format = "HH:mm:ss", example = "09:00:00")
    private LocalTime startTime;

    @Schema(description = "Hora de inicio", type = "string", format = "HH:mm:ss", example = "10:00:00")
    private LocalTime endTime;

    public RegistrationSummaryDTO(UserRegistrationEntity registration) {
        this.className = String.valueOf(registration.getSchedule().getClassType().getClassName());
        this.days = registration.getSchedule().getDays().stream().map(dow -> dow.getDay()).collect(Collectors.toList());
        this.endTime = registration.getSchedule().getEndTime();
        this.startTime = registration.getSchedule().getStartTime();
        this.userName = registration.getUser().getUsername();
    }
}
