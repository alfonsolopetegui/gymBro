package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.ScheduleDayEntity;
import com.myCompany.gymBro.persistence.entity.UserRegistrationEntity;
import com.myCompany.gymBro.persistence.enums.DayOfWeek;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class RegistrationSummaryDTO {

    //user name
    private String userName;
    //className
    private String className;
    //Days
    private List<DayOfWeek> days;
    //startTime
    private LocalTime startTime;
    //endTime
    private LocalTime endTime;

    public RegistrationSummaryDTO(UserRegistrationEntity registration) {
        this.className = String.valueOf(registration.getSchedule().getClassType().getClassName());
        this.days = registration.getSchedule().getDays().stream().map(dow -> dow.getDay()).collect(Collectors.toList());
        this.endTime = registration.getSchedule().getEndTime();
        this.startTime = registration.getSchedule().getStartTime();
        this.userName = registration.getUser().getUsername();
    }
}
