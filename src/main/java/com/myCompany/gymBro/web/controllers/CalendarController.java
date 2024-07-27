package com.myCompany.gymBro.web.controllers;



import com.myCompany.gymBro.service.GoogleCalendarService;
import com.myCompany.gymBro.service.dto.EventDTO;
import com.myCompany.gymBro.web.config.GoogleApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    private final GoogleCalendarService calendarService;

    @Autowired
    private GoogleApiProperties googleApiProperties;

    public CalendarController(GoogleCalendarService calendarService) {
        this.calendarService = calendarService;
    }


    @GetMapping("/authorize")
    public RedirectView authorize() {
        String authorizationUrl = String.format(
                "%s?response_type=code&client_id=%s&redirect_uri=%s&scope=%s",
                googleApiProperties.getAuthorizationEndpoint(),
                googleApiProperties.getId(),
                googleApiProperties.getRedirectUri(),
                "https://www.googleapis.com/auth/calendar"
        );
        return new RedirectView(authorizationUrl);
    }



    @GetMapping("/oauth2/callback")
    public ResponseEntity<String> oauth2Callback(@RequestParam("code") String code) {

        try {
            // Aquí intercambias el código por un token de acceso
            String accessToken = exchangeCodeForAccessToken(code);


            return ResponseEntity.ok("Token de acceso recibido: " + accessToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al manejar la respuesta de OAuth2: " + e.getMessage());
        }
    }


    @PostMapping("/createEvent")
    public ResponseEntity<String> createEvent(@RequestHeader("Authorization") String token,
                                              @RequestBody EventDTO eventDTO) {
        try {
            // Extrae el token de acceso del encabezado Authorization
            String extractedToken = extractToken(token);

            // Utiliza el servicio para crear el evento
            calendarService.createEvent(extractedToken, eventDTO);
            return ResponseEntity.ok("Evento creado con éxito");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el evento");
        }


    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Elimina el prefijo "Bearer "
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }


    private String exchangeCodeForAccessToken(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("client_id", googleApiProperties.getId());
        map.add("client_secret", googleApiProperties.getSecret());
        map.add("redirect_uri", googleApiProperties.getRedirectUri());
        map.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("https://oauth2.googleapis.com/token", request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("access_token")) {
                return responseBody.get("access_token").toString();
            } else {
                throw new Exception("No se pudo obtener el token de acceso de la respuesta");
            }
        } else {
            throw new Exception("Error en la solicitud de token de acceso: " + response.getStatusCode());
        }
    }


}
