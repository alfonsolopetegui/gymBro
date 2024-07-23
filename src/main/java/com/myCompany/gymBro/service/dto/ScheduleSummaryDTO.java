package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import com.myCompany.gymBro.persistence.enums.DayOfWeek;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleSummaryDTO {
    private List<String> days;
    private LocalTime startTime;
    private LocalTime endTime;

    public ScheduleSummaryDTO(ScheduleEntity schedule) {
        this.days = schedule.getDays().stream().map(day -> day.getDay().toString()).collect(Collectors.toList());
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
    }
}
