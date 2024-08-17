package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.ScheduleEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
public class ScheduleDTO {

    private UUID scheduleId;

    private String className;

    private LocalTime startTime;

    private LocalTime endTime;

    private List<DayOfWeek> days;

    private int numberOfUsers;

    private List<String> userNames;

    public ScheduleDTO(ScheduleEntity schedule) {
        this.className = schedule.getClassType().getClassName();
        this.days = schedule.getDays();
        this.endTime = schedule.getEndTime();
        this.numberOfUsers = schedule.getUserRegistrations().size();
        this.scheduleId = schedule.getScheduleId();
        this.startTime = schedule.getStartTime();

        // Convertir UserRegistrationEntity a nombres de usuario
        this.userNames = schedule.getUserRegistrations().stream()
                .map(userRegistration -> userRegistration.getUser().getUsername())
                .collect(Collectors.toList());
    }
}
