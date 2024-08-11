package com.myCompany.gymBro.web.controllers;


import com.myCompany.gymBro.exception.AccessDeniedException;
import com.myCompany.gymBro.persistence.entity.UserRegistrationEntity;
import com.myCompany.gymBro.service.UserRegistrationService;

import com.myCompany.gymBro.service.dto.ClassCreationDTO;
import com.myCompany.gymBro.service.dto.RegistrationCreationDTO;
import com.myCompany.gymBro.service.dto.RegistrationSummaryDTO;
import com.myCompany.gymBro.web.config.JwtUtils;
import com.myCompany.gymBro.web.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/registrations")
@Tag(name = "UserRegistration Controller", description = "Operaciones relacionadas con los registros de los usuarios a los horarios disponibles en el gimnasio")
@Validated
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserRegistrationController(UserRegistrationService userRegistrationService, JwtUtils jwtUtils) {
        this.userRegistrationService = userRegistrationService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/byUserId/{userId}")
    @Operation(summary = "Devuelve los registros a clase de un usuario", description = "Podrás ver todos los registros a clases de un usuario a partir de su Id")
    public ResponseEntity<ApiResponse<List<RegistrationSummaryDTO>>> getAllByUserId(@PathVariable UUID userId) {
        ApiResponse<List<RegistrationSummaryDTO>> response = this.userRegistrationService.getAllRegistrationsByUserId(userId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }


    //Crear registro de usuario a una clase
    @PostMapping("/save")
    @Operation(summary = "Guardar un registro a un horario", description = "Guarda un nuevo registro a un horario de clase específico, en la base de datos, ingresando los datos utilizando el RegistrationCreationDTO. Si el usuario tiene createCalendarEvents = true, creará el correspondiente evento en Google Calendar")
    public ResponseEntity<ApiResponse<RegistrationSummaryDTO>> saveRegistration(
            @Parameter(
                    description = "Datos del registro a guardar, en formato RegistrationCreationDTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegistrationCreationDTO.class))
            )
            @RequestBody @Valid RegistrationCreationDTO registration,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);  // Quitar "Bearer " del encabezado
        String authenticatedUserId = jwtUtils.extractUserId(token);

        if (!authenticatedUserId.equals(registration.getUserId().toString())) {
            throw new AccessDeniedException("No tienes permisos para realizar esta acción");
        }


        ApiResponse<RegistrationSummaryDTO> response = this.userRegistrationService.saveRegistration(registration);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    //Cancelar registro de usuario a una clase
    @DeleteMapping("/cancel/{registrationId}")
    @Operation(summary = "Cancelar un registro a un horario", description = "Cancela el registro de un usuario a un horario de una clase específica")
    public ResponseEntity<ApiResponse<Void>> cancelRegistration(
            @PathVariable UUID registrationId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);  // Quitar "Bearer " del encabezado
        String authenticatedUserId = jwtUtils.extractUserId(token);

        UserRegistrationEntity registration = this.userRegistrationService.getById(registrationId).getData();

        if (!authenticatedUserId.equals(registration.getUser().getUserId().toString())) {
            throw new AccessDeniedException("No tienes permisos para realizar esta acción");
        }

        ApiResponse<Void> response = this.userRegistrationService.cancelRegistration(registrationId);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }
}
