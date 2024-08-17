package com.myCompany.gymBro.service;

import com.myCompany.gymBro.exception.RegistrationNotFoundException;
import com.myCompany.gymBro.exception.ScheduleNotFoundException;
import com.myCompany.gymBro.exception.TokenNotFoundException;
import com.myCompany.gymBro.exception.UserNotFoundException;
import com.myCompany.gymBro.persistence.entity.*;
import com.myCompany.gymBro.persistence.repository.GoogleTokenRepository;
import com.myCompany.gymBro.persistence.repository.ScheduleRepository;
import com.myCompany.gymBro.persistence.repository.UserRegistrationRepository;
import com.myCompany.gymBro.persistence.repository.UserRepository;
import com.myCompany.gymBro.service.dto.EventDTO;
import com.myCompany.gymBro.service.dto.RegistrationCreationDTO;
import com.myCompany.gymBro.service.dto.RegistrationSummaryDTO;
import com.myCompany.gymBro.utils.ValidationUtils;
import com.myCompany.gymBro.web.response.ApiResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


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

    public ApiResponse<List<RegistrationSummaryDTO>> getAllRegistrationsByUserId(UUID userId) {
        try {
            // Cambia findAllById por el método adecuado, asumiendo que userId no es la clave primaria
            List<UserRegistrationEntity> registrationEntities = this.userRegistrationRepository.findAllByUser_UserId(userId);

            // Usar streams para mapear directamente las entidades a DTOs
            List<RegistrationSummaryDTO> registrationSummaryDTOList = registrationEntities.stream()
                    .map(RegistrationSummaryDTO::new)
                    .collect(Collectors.toList());

            return new ApiResponse<>("Se muestra lista de registros a clases", 200, registrationSummaryDTOList);

        } catch (DataAccessException e) { // Ejemplo de excepción específica de Spring
            e.printStackTrace();
            return new ApiResponse<>("Error al buscar los registros en la base de datos", 500, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>("Error inesperado al buscar los registros", 500, null);
        }
    }


    public ApiResponse<UserRegistrationEntity> getById(UUID registrationId) {

        UserRegistrationEntity reg = this.userRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new RegistrationNotFoundException("No encontramos ese registro"));

        return new ApiResponse<>("Registro encontrado", 200, reg);

    }

    //Método para crear registros de usuarios a clases con horarios definidos (schedules).
    //Si el usuario tiene configurado para crear eventos en calendar, ejecutará handleCalendarEventCreation
    //Al ser Transactional, si alguna parte falla, se cancelará el método completo
    @Transactional
    public ApiResponse<RegistrationSummaryDTO> saveRegistration(RegistrationCreationDTO registration) {

        System.out.println("Entro al servicio");

        UUID userId = registration.getUserId();
        UUID scheduleId = registration.getScheduleId();

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

        System.out.println(userRegistrationEntity);

        if (userEntity.getCreateCalendarEvents()) {
            try {
                handleCalendarEventCreation(userEntity, scheduleEntity, registration);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al intentar crear el Evento");
            }
        }

        return new ApiResponse<>("Registro a clase creado correctamente", 200, registrationSummary);
    }

    //Se encarga de validar el token del usuario, o de renovarlo si es necesario
    private void handleCalendarEventCreation(UserEntity userEntity, ScheduleEntity scheduleEntity, RegistrationCreationDTO registration) {
        System.out.println("Entro a validar tokens");
        System.out.println("UserId: " + userEntity.getUserId());
        System.out.println(userEntity.getGoogleToken().getAccessToken());

        UUID id = registration.getUserId();

        // Busca el token en la base de datos
        GoogleTokenEntity tokenEntity = googleTokenRepository.findByUser_UserId(id)
                .orElseThrow(() -> new TokenNotFoundException("No encontramos el token"));


        System.out.println("TokenEntity: " + tokenEntity);

        if (tokenEntity != null) {
            String accessToken = tokenEntity.getAccessToken();
            String refreshToken = tokenEntity.getRefreshToken();

            System.out.println("AccessToken: " + accessToken);
            System.out.println("RefreshToken: " + refreshToken);

            // Verifica si el token es válido
            if (accessToken != null && !accessToken.isEmpty()) {
                boolean isValid = googleTokenService.isTokenValid(accessToken);
                System.out.println("Token válido: " + isValid);

                if (isValid) {
                    createEventInCalendar(accessToken, scheduleEntity, registration);
                } else if (refreshToken != null && !refreshToken.isEmpty()) {
                    System.out.println("Token inválido, intentando renovar...");
                    try {
                        accessToken = googleTokenService.refreshToken(refreshToken);
                        tokenEntity.setAccessToken(accessToken);
                        googleTokenRepository.save(tokenEntity);
                        System.out.println("Nuevo AccessToken: " + accessToken);
                        createEventInCalendar(accessToken, scheduleEntity, registration);
                    } catch (Exception e) {
                        throw new RuntimeException("Error al renovar el token de acceso", e);
                    }
                } else {
                    throw new RuntimeException("Token de acceso y de actualización inválidos o ausentes");
                }
            } else {
                throw new RuntimeException("Token de acceso y de actualización ausentes");
            }
        } else {
            throw new RuntimeException("Tokens de acceso y de actualización ausentes");
        }
    }

    //LLama a las funciones que crean el EventDTO, para luego llamar a la que crea el Evento
    private void createEventInCalendar(String accessToken, ScheduleEntity scheduleEntity, RegistrationCreationDTO registration) {
        try {
            System.out.println("intentando crear DTO");
            EventDTO eventDTO = googleCalendarService.createEventDTO(scheduleEntity, registration);
            System.out.println("intentando crear el evento");
            googleCalendarService.createEvent(accessToken, eventDTO);
        } catch (IOException e) {
            System.err.println("Error al crear el evento en Google Calendar: " + e.getMessage());
        }
    }

    //Metodo para eliminar un registro a una clase
    public ApiResponse<Void> cancelRegistration(UUID registrationId) {

        //Busco el registro por su Id
        UserRegistrationEntity registrationEntity = this.userRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new RegistrationNotFoundException("Registro no encontrado"));

        try {
            // Eliminar el registro
            userRegistrationRepository.deleteById(registrationId);
        } catch (Exception e) {
            // Manejar excepciones inesperadas
            throw new RuntimeException("Error al cancelar el registro", e);
        }


        return new ApiResponse<>("Registro cancelado correctamente", 200, null);
    }


}
