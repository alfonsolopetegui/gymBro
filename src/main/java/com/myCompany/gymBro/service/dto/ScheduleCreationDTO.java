package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.ClassEntity;
import com.myCompany.gymBro.persistence.entity.ScheduleDayEntity;
import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import com.myCompany.gymBro.persistence.entity.UserRegistrationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleCreationDTO {

    private String classId;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<String> days;
    private int maxRegistrations;

    public ScheduleCreationDTO(ScheduleEntity schedule) {
        this.classId = String.valueOf(schedule.getClassType().getClassId());
        this.days = schedule.getDays().stream().map(day -> day.getDay().toString()).collect(Collectors.toList());
        this.endTime = schedule.getEndTime();
        this.maxRegistrations = schedule.getMaxRegistrations();
        this.startTime = schedule.getStartTime();
    }
}
