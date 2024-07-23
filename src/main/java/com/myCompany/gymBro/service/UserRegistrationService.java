package com.myCompany.gymBro.service;

import com.myCompany.gymBro.exception.RegistrationNotFoundException;
import com.myCompany.gymBro.exception.ScheduleNotFoundException;
import com.myCompany.gymBro.exception.UserNotFoundException;
import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import com.myCompany.gymBro.persistence.entity.UserEntity;
import com.myCompany.gymBro.persistence.entity.UserRegistrationEntity;
import com.myCompany.gymBro.persistence.repository.ScheduleRepository;
import com.myCompany.gymBro.persistence.repository.UserRegistrationRepository;
import com.myCompany.gymBro.persistence.repository.UserRepository;
import com.myCompany.gymBro.service.dto.RegistrationCreationDTO;
import com.myCompany.gymBro.service.dto.RegistrationSummaryDTO;
import com.myCompany.gymBro.utils.ValidationUtils;
import com.myCompany.gymBro.web.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserRegistrationService {

    private final UserRegistrationRepository userRegistrationRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;


    public UserRegistrationService(UserRegistrationRepository userRegistrationRepository, UserRepository userRepository, ScheduleRepository scheduleRepository) {
        this.userRegistrationRepository = userRegistrationRepository;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
    }



    //Metodo para que el usuario se registre en una clase
    public ApiResponse<RegistrationSummaryDTO> saveRegistration(RegistrationCreationDTO registration) {

        // Verifico que el userId y el scheduleId sean UUID válidos
        if (!ValidationUtils.isValidUUID(registration.getUserId())) {
            throw new IllegalArgumentException("El ID del usuario no es un UUID válido");
        }
        if (!ValidationUtils.isValidUUID(registration.getScheduleId())) {
            throw new IllegalArgumentException("El ID del schedule no es un UUID válido");
        }

        UUID userId = UUID.fromString(registration.getUserId());
        UUID scheduleId = UUID.fromString(registration.getScheduleId());

        // Verifico si el usuario ya está registrado en esta clase
        if (this.userRegistrationRepository.existsByUserIdAndScheduleId(userId, scheduleId)) {
            throw new IllegalArgumentException("El usuario ya está registrado a esta clase");
        }

        // Busco el usuario por su Id
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        // Busco el schedule por su Id
        ScheduleEntity scheduleEntity = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("Horario de clase no encontrado"));

        //Verifico que la clase no esté completa
        if (scheduleEntity.isRegistrationFull()) {
            throw new IllegalArgumentException("No puedes registrarte porque la clase ya está completa");
        }

        UserRegistrationEntity userRegistrationEntity = new UserRegistrationEntity();
        userRegistrationEntity.setUser(userEntity);
        userRegistrationEntity.setSchedule(scheduleEntity);
        userRegistrationEntity.setRegistrationDate(LocalDateTime.now());

        UserRegistrationEntity savedRegistration = this.userRegistrationRepository.save(userRegistrationEntity);
        RegistrationSummaryDTO registrationSummary = new RegistrationSummaryDTO(savedRegistration);

        return new ApiResponse<>("Registro a clase creado correctamente", 200, registrationSummary);
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
