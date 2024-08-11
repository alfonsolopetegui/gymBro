package com.myCompany.gymBro.service;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.myCompany.gymBro.persistence.entity.GoogleTokenEntity;
import com.myCompany.gymBro.persistence.entity.ScheduleDayEntity;
import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import com.myCompany.gymBro.persistence.repository.GoogleTokenRepository;
import com.myCompany.gymBro.service.dto.EventDTO;
import com.myCompany.gymBro.service.dto.RegistrationCreationDTO;
import com.myCompany.gymBro.web.response.ApiResponse;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoogleCalendarService {

    private final GoogleTokenRepository googleTokenRepository;

    public GoogleCalendarService(GoogleTokenRepository googleTokenRepository) {
        this.googleTokenRepository = googleTokenRepository;
    }

    public void createEvent(String token, EventDTO eventDTO) throws IOException {

        System.out.println("Token: " + token);
        System.out.println("eventDTO: " + eventDTO);

        try {

            // Configura el cliente de la API con el token de acceso
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            GoogleCredentials credentials = GoogleCredentials.create(new AccessToken(token, null));


            // Define el HttpRequestInitializer que agrega el token de acceso a las solicitudes
            HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) throws IOException {
                    // Agrega el token de acceso a los encabezados de la solicitud
                    HttpHeaders headers = new HttpHeaders();
                    headers.setAuthorization("Bearer " + token);
                    request.setHeaders(headers);
                }
            };

            System.out.println("intentando crear cliente");

            Calendar service = new Calendar.Builder(httpTransport, jsonFactory, httpRequestInitializer)
                    .setApplicationName("Gymbro project")
                    .build();

            System.out.println("intentando costruir evento");

            // Construye el evento
            Event event = new Event()
                    .setSummary(eventDTO.getSummary())
                    .setLocation(eventDTO.getLocation())
                    .setDescription(eventDTO.getDescription());

            System.out.println(event.getSummary());
            System.out.println(event.getLocation());
            System.out.println(event.getDescription());

            EventDateTime start = new EventDateTime()
                    .setDateTime(new DateTime(eventDTO.getStart().getDateTime()))
                    .setTimeZone(eventDTO.getStart().getTimeZone());
            event.setStart(start);

            System.out.println("Start: " + event.getStart().getDateTime());

            EventDateTime end = new EventDateTime()
                    .setDateTime(new DateTime(eventDTO.getEnd().getDateTime()))
                    .setTimeZone(eventDTO.getEnd().getTimeZone());
            event.setEnd(end);

            System.out.println("End: " + event.getEnd().getDateTime());




            // Imprime el valor de entrada para verificar
            System.out.println("Input DateTime String: " + eventDTO.getStart().getDateTime());

            // Define el formato de entrada con nanosegundos
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");

            // Intenta parsear la cadena
            LocalDateTime startDateTimeObj = LocalDateTime.parse(eventDTO.getStart().getDateTime(), formatter);
            System.out.println("Parsed LocalDateTime: " + startDateTimeObj);

            // Calcula la fecha de finalización
            LocalDateTime endDateTime = startDateTimeObj.plusMonths(1);
            System.out.println("End DateTime: " + endDateTime);

            // Formatea la fecha de finalización para la recurrencia
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
            String endDateTimeStr = endDateTime.format(outputFormatter);

            System.out.println("End Recurrence Date: " + endDateTimeStr);

            // Construye la regla de recurrencia
            String recurrenceRule = "RRULE:FREQ=WEEKLY;BYDAY=" + String.join(",", eventDTO.getDays()) + ";UNTIL=" + endDateTimeStr;
            System.out.println("Recurrence Rule: " + recurrenceRule);




            // Configura la recurrencia
            event.setRecurrence(Collections.singletonList(recurrenceRule));


            System.out.println("EventRec: " + event.getRecurrence());

            System.out.println("intentando crear evento en calendar");

            // Inserta el evento en el calendario
            service.events().insert("primary", event).execute();
        } catch (IOException e) {
            System.err.println("Error al crear el evento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public EventDTO createEventDTO(ScheduleEntity scheduleEntity, RegistrationCreationDTO registration) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setSummary("Clase de " + scheduleEntity.getClassType().getClassName());
        eventDTO.setLocation("GymBro");
        eventDTO.setDescription(scheduleEntity.getClassType().getClassDescription());
        eventDTO.setDays(getFormattedDays(scheduleEntity.getDays()));

        // Convertir los días personalizados a DayOfWeek
        List<DayOfWeek> daysOfWeek = getDaysOfWeek(scheduleEntity.getDays());

        // Extraer la hora de inicio
        LocalTime startTime = scheduleEntity.getStartTime();

        // Calcular originalStartDate
        EventDTO.EventDateTime originalStartDate = calculateOriginalStartDate(daysOfWeek, startTime);
        eventDTO.setStart(originalStartDate);

        // Calcular el endDate sumando 1 hora a la hora de inicio
        LocalDateTime startDateTimeObj = LocalDateTime.parse(originalStartDate.getDateTime());
        LocalDateTime endDateTimeObj = startDateTimeObj.plusHours(1);
        EventDTO.EventDateTime endDateTime = new EventDTO.EventDateTime(endDateTimeObj.toString(), "America/Argentina/Buenos_Aires");
        eventDTO.setEnd(endDateTime);

        return eventDTO;
    }

    // Calcula la fecha de finalización (un mes después de la fecha original)
    private String calculateRecurrenceEndDate(String start) {
        LocalDate originalStartDate = LocalDate.parse(start.substring(0, 10)); // Extrae solo la fecha
        LocalDate endDate = originalStartDate.plus(1, ChronoUnit.MONTHS); // Suma un mes
        return endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "T000000Z"; // Formato: YYYYMMDDTHHMMSSZ
    }

    public ApiResponse<Void> saveToken(GoogleTokenEntity token) {
        try {
            this.googleTokenRepository.save(token);
            return new ApiResponse<>("Token guardado co éxito", 200, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>("Error al guardar el token", 500, null);
        }
    }

    public EventDTO.EventDateTime calculateOriginalStartDate(List<DayOfWeek> daysOfWeek, LocalTime startTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTimeObj = now.withHour(startTime.getHour()).withMinute(startTime.getMinute()).withSecond(0);

        for (DayOfWeek day : daysOfWeek) {
            LocalDateTime firstOccurrence = startDateTimeObj.with(TemporalAdjusters.nextOrSame(day));
            if (firstOccurrence.isAfter(now)) {
                return new EventDTO.EventDateTime(firstOccurrence.toString(), "America/Argentina/Buenos_Aires");
            }
        }

        return new EventDTO.EventDateTime(now.toString(), "America/Argentina/Buenos_Aires");
    }

    private List<DayOfWeek> getDaysOfWeek(List<ScheduleDayEntity> days) {
        return days.stream()
                .map(day -> {
                    switch (day.getDay()) {
                        case MONDAY:
                            return DayOfWeek.MONDAY;
                        case TUESDAY:
                            return DayOfWeek.TUESDAY;
                        case WEDNESDAY:
                            return DayOfWeek.WEDNESDAY;
                        case THURSDAY:
                            return DayOfWeek.THURSDAY;
                        case FRIDAY:
                            return DayOfWeek.FRIDAY;
                        case SATURDAY:
                            return DayOfWeek.SATURDAY;
                        case SUNDAY:
                            return DayOfWeek.SUNDAY;
                        default:
                            throw new IllegalArgumentException("Invalid day of the week: " + day);
                    }
                })
                .collect(Collectors.toList());
    }

    private List<String> getFormattedDays(List<ScheduleDayEntity> days) {
        return days.stream()
                .map(day -> {
                    switch (day.getDay()) {
                        case MONDAY:
                            return "MO";
                        case TUESDAY:
                            return "TU";
                        case WEDNESDAY:
                            return "WE";
                        case THURSDAY:
                            return "TH";
                        case FRIDAY:
                            return "FR";
                        case SATURDAY:
                            return "SA";
                        case SUNDAY:
                            return "SU";
                        default:
                            throw new IllegalArgumentException("Invalid day of the week: " + day);
                    }
                })
                .collect(Collectors.toList());
    }

}

