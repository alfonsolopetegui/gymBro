package com.myCompany.gymBro.service;

import com.myCompany.gymBro.exception.RegistrationNotFoundException;
import com.myCompany.gymBro.exception.ScheduleNotFoundException;
import com.myCompany.gymBro.exception.UserNotFoundException;
import com.myCompany.gymBro.persistence.entity.GoogleTokenEntity;
import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import com.myCompany.gymBro.persistence.entity.UserEntity;
import com.myCompany.gymBro.persistence.entity.UserRegistrationEntity;
import com.myCompany.gymBro.persistence.repository.GoogleTokenRepository;
import com.myCompany.gymBro.persistence.repository.ScheduleRepository;
import com.myCompany.gymBro.persistence.repository.UserRegistrationRepository;
import com.myCompany.gymBro.persistence.repository.UserRepository;
import com.myCompany.gymBro.service.dto.EventDTO;
import com.myCompany.gymBro.service.dto.RegistrationAndEventDTO;
import com.myCompany.gymBro.service.dto.RegistrationSummaryDTO;
import com.myCompany.gymBro.utils.ValidationUtils;
import com.myCompany.gymBro.web.response.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserRegistrationService {

    private final UserRegistrationRepository userRegistrationRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final GoogleTokenRepository googleTokenRepository;
    private final GoogleCalendarService googleCalendarService;
    private final GoogleTokenService googleTokenService;


    public UserRegistrationService(UserRegistrationRepository userRegistrationRepository, UserRepository userRepository, ScheduleRepository scheduleRepository, GoogleTokenRepository googleTokenRepository, GoogleCalendarService googleCalendarService, GoogleTokenService googleTokenService) {
        this.userRegistrationRepository = userRegistrationRepository;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
        this.googleTokenRepository = googleTokenRepository;
        this.googleCalendarService = googleCalendarService;
        this.googleTokenService = googleTokenService;
    }


    @Transactional
    public ApiResponse<RegistrationSummaryDTO> saveRegistration(RegistrationAndEventDTO registration) {
        // Verifico que el userId y el scheduleId sean UUID válidos
        if (!ValidationUtils.isValidUUID(registration.getUserId())) {
            throw new IllegalArgumentException("El ID del usuario no es un UUID válido");
        }
        if (!ValidationUtils.isValidUUID(registration.getScheduleId())) {
            throw new IllegalArgumentException("El ID del schedule no es un UUID válido");
        }

        UUID userId = UUID.fromString(registration.getUserId());
        UUID scheduleId = UUID.fromString(registration.getScheduleId());

        // Busco el usuario por su Id
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        // Busco el schedule por su Id
        ScheduleEntity scheduleEntity = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("Horario de clase no encontrado"));

        // Verifico si el usuario ya está registrado en esta clase
        if (this.userRegistrationRepository.existsByUserIdAndScheduleId(userId, scheduleId)) {
            throw new IllegalArgumentException("El usuario ya está registrado a esta clase");
        }

        // Verifico que la clase no esté completa
        if (scheduleEntity.isRegistrationFull()) {
            throw new IllegalArgumentException("No puedes registrarte porque la clase ya está completa");
        }

        UserRegistrationEntity userRegistrationEntity = new UserRegistrationEntity();
        userRegistrationEntity.setUser(userEntity);
        userRegistrationEntity.setSchedule(scheduleEntity);
        userRegistrationEntity.setRegistrationDate(LocalDateTime.now());

        UserRegistrationEntity savedRegistration = this.userRegistrationRepository.save(userRegistrationEntity);
        RegistrationSummaryDTO registrationSummary = new RegistrationSummaryDTO(savedRegistration);

        if (userEntity.getCreateCalendarEvents()) {
            handleCalendarEventCreation(userEntity, scheduleEntity, registration);
        }

        return new ApiResponse<>("Registro a clase creado correctamente", 200, registrationSummary);
    }

    //Se encarga de validar el token del usuario, o de renovarlo si es necesario
    private void handleCalendarEventCreation(UserEntity userEntity, ScheduleEntity scheduleEntity, RegistrationAndEventDTO registration) {
        GoogleTokenEntity tokenEntity = this.googleTokenRepository.findByUser_UserId(userEntity.getUserId())
                .orElse(null);

        if (tokenEntity != null) {
            String accessToken = tokenEntity.getAccessToken();
            String refreshToken = tokenEntity.getRefreshToken();

            // Verifica si el token es válido
            if (accessToken != null && !accessToken.isEmpty() && googleTokenService.isTokenValid(accessToken)) {
                createEventInCalendar(accessToken, scheduleEntity, registration);
            } else if (refreshToken != null && !refreshToken.isEmpty()) {
                try {
                    accessToken = googleTokenService.refreshToken(refreshToken);
                    tokenEntity.setAccessToken(accessToken);
                    googleTokenRepository.save(tokenEntity);
                    createEventInCalendar(accessToken, scheduleEntity, registration);
                } catch (Exception e) {
                    throw new RuntimeException("Error al renovar el token de acceso", e);
                }
            } else {
                throw new RuntimeException("Token de acceso y de actualización inválidos o ausentes");
            }
        } else {
            throw new RuntimeException("Tokens de acceso y de actualización ausentes");
        }
    }


    //Se encarga de crear el EventDTO, que luego será la data para crear el evento en Calendar
    private void createEventInCalendar(String accessToken, ScheduleEntity scheduleEntity, RegistrationAndEventDTO registration) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setSummary("Clase de " + scheduleEntity.getClassType().getClassName());
        eventDTO.setLocation("GymBro");
        eventDTO.setDescription(scheduleEntity.getClassType().getClassDescription());
        eventDTO.setStart(new EventDTO.EventDateTime(registration.getStart().getDateTime(), "America/Argentina/Buenos_Aires"));
        eventDTO.setEnd(new EventDTO.EventDateTime(registration.getEnd().getDateTime(), "America/Argentina/Buenos_Aires"));

        try {
            googleCalendarService.createEvent(accessToken, eventDTO);
        } catch (IOException e) {
            throw new RuntimeException("Error al crear el evento en el calendario", e);
        }
    }



    //Metodo para eliminar un registro a una clase
    public ApiResponse<Void> cancelRegistration(String registrationId) {

        // Verifico que el userId y el scheduleId sean UUID válidos
        if (!ValidationUtils.isValidUUID(registrationId)) {
            throw new IllegalArgumentException("El ID proporcionado no es un UUID válido");
        }

        UUID regId = UUID.fromString(registrationId);

        //Busco el registro por su Id
        UserRegistrationEntity registrationEntity = this.userRegistrationRepository.findById(regId)
                .orElseThrow(() -> new RegistrationNotFoundException("Registro no encontrado"));


        try {
            // Eliminar el registro
            userRegistrationRepository.deleteById(regId);
        } catch (Exception e) {
            // Manejar excepciones inesperadas
            throw new RuntimeException("Error al cancelar el registro", e);
        }


        return new ApiResponse<>("Registro cancelado correctamente", 200, null);
    }


}
