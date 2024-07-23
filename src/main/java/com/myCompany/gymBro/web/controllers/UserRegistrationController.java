package com.myCompany.gymBro.web.controllers;

import com.myCompany.gymBro.persistence.entity.UserRegistrationEntity;
import com.myCompany.gymBro.service.UserRegistrationService;
import com.myCompany.gymBro.service.dto.RegistrationCreationDTO;
import com.myCompany.gymBro.service.dto.RegistrationSummaryDTO;
import com.myCompany.gymBro.web.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registrations")
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    @Autowired
    public UserRegistrationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    //Crear registro de usuario a una clase
    @PostMapping
    public ResponseEntity<ApiResponse<RegistrationSummaryDTO>> saveRegistration(@RequestBody RegistrationCreationDTO registrationCreationDTO) {
        ApiResponse<RegistrationSummaryDTO> response = this.userRegistrationService.saveRegistration(registrationCreationDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    //Cancelar registro de usuario a una clase
    @DeleteMapping("/cancel/{registrationId}")
    public ResponseEntity<ApiResponse<Void>> cancelRegistration(@PathVariable String registrationId) {
        ApiResponse<Void> response = this.userRegistrationService.cancelRegistration(registrationId);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }
}
