package com.myCompany.gymBro.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class EventDTO {
    private String summary;
    private String location;
    private String description;
    private EventDateTime start;
    private EventDateTime end;

    private String days;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class EventDateTime {
        private String dateTime;
        private String timeZone;

        // Constructor que inicializa los campos
        public EventDateTime(String dateTime, String timeZone) {
            this.dateTime = dateTime;
            this.timeZone = timeZone;
        }
    }


    @Override
    public String toString() {
        return "EventDTO{" +
                "days=" + days +
                ", summary='" + summary + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", start=" + start.dateTime +
                ", end=" + end.dateTime +
                '}';
    }
}
