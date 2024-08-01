package com.myCompany.gymBro.service.dto;

import com.google.api.services.calendar.model.EventDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationAndEventDTO {

    //Registration
    private String userId;
    private String scheduleId;

    //Event
    private String summary;
    private String location;
    private String description;
    private EventDateTime start;
    private EventDateTime end;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class EventDateTime {
        private String dateTime;
        private String timeZone;

        public EventDateTime(String dateTime, String timeZone) {
            this.dateTime = dateTime;
            this.timeZone = timeZone;
        }
    }

}
