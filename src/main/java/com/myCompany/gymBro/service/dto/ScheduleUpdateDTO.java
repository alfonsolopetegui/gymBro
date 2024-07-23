package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleUpdateDTO {

    private String scheduleId;
    private String classId;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<String> days;
    private int maxRegistrations;

    public ScheduleUpdateDTO(ScheduleEntity schedule) {
        this.scheduleId = String.valueOf(schedule.getScheduleId());
        this.classId = String.valueOf(schedule.getClassType().getClassId());
        this.days = schedule.getDays().stream().map(day -> day.getDay().toString()).collect(Collectors.toList());
        this.endTime = schedule.getEndTime();
        this.maxRegistrations = schedule.getMaxRegistrations();
        this.startTime = schedule.getStartTime();
    }
}
