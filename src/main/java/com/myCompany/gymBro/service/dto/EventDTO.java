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
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

}
