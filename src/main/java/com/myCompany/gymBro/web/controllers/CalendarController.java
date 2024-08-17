package com.myCompany.gymBro.web.controllers;

import com.myCompany.gymBro.service.GoogleCalendarService;

import com.myCompany.gymBro.service.GoogleTokenService;
import com.myCompany.gymBro.web.config.GoogleApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.view.RedirectView;

import java.util.Base64;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class CalendarController {

    private final GoogleCalendarService calendarService;
    private final GoogleTokenService googleTokenService;

    @Autowired
    private GoogleApiProperties googleApiProperties;


    public CalendarController(GoogleCalendarService calendarService, GoogleTokenService googleTokenService) {
        this.calendarService = calendarService;
        this.googleTokenService = googleTokenService;
    }

    //Si tenes un usuario registrado, este endpoint te redirige a Calendar, para generar permisos a la app, y
    //devolverá un token que será guardado en la base de datos, para luego crear los eventos al momento
    //de registrarse a un horario de una clase
    @GetMapping("calendar/authorize/{userId}")
    public RedirectView authorize(@PathVariable UUID userId) {

        // Codifica el estado
        String state = Base64.getEncoder().encodeToString(userId.toString().getBytes());

        String authorizationUrl = String.format(
                "%s?response_type=code&client_id=%s&redirect_uri=%s&scope=%s&state=%s&access_type=offline",
                googleApiProperties.getAuthorizationEndpoint(),
                googleApiProperties.getId(),
                googleApiProperties.getRedirectUri(),
                "https://www.googleapis.com/auth/calendar",
                state
        );
        return new RedirectView(authorizationUrl);
    }



    @GetMapping("/oauth2/callback")
    public ResponseEntity<String> oauth2Callback(@RequestParam("code") String code, @RequestParam("state") String state) {
        try {
            // Decodifica el parámetro state para obtener el ID del usuario
            String userId = new String(Base64.getDecoder().decode(state));

            // Llama al servicio para manejar el intercambio de código y guardar el token
            googleTokenService.handleOAuth2Callback(code, userId);

            return ResponseEntity.ok("Token de acceso recibido y guardado para el usuario: " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al manejar la respuesta de OAuth2: " + e.getMessage());
        }
    }


}
