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
import com.myCompany.gymBro.service.dto.EventDTO;
import org.springframework.stereotype.Service;


import java.io.IOException;

@Service
public class GoogleCalendarService {

    public void createEvent(String token, EventDTO eventDTO) throws IOException {

        try {

            // Configura el cliente de la API con el token de acceso
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            GoogleCredentials credentials = GoogleCredentials.create(new AccessToken(token, null));


            System.out.println("httpTransport: " + httpTransport);
            System.out.println("jsonFactory: " + jsonFactory);
            System.out.println("credentials: " + credentials);

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

            Calendar service = new Calendar.Builder(httpTransport, jsonFactory, httpRequestInitializer)
                    .setApplicationName("Gymbro project")
                    .build();

            // Construye el evento
            Event event = new Event()
                    .setSummary(eventDTO.getSummary())
                    .setLocation(eventDTO.getLocation())
                    .setDescription(eventDTO.getDescription());

            EventDateTime start = new EventDateTime()
                    .setDateTime(new DateTime(eventDTO.getStart().getDateTime()))
                    .setTimeZone(eventDTO.getStart().getTimeZone());
            event.setStart(start);

            EventDateTime end = new EventDateTime()
                    .setDateTime(new DateTime(eventDTO.getEnd().getDateTime()))
                    .setTimeZone(eventDTO.getEnd().getTimeZone());
            event.setEnd(end);

            // Inserta el evento en el calendario
            service.events().insert("primary", event).execute();
        } catch (IOException e) {
            System.err.println("Error al crear el evento: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

