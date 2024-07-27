package com.myCompany.gymBro.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class EventDTO {
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
    }

}
