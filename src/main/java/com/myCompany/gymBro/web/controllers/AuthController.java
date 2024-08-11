package com.myCompany.gymBro.web.controllers;


import com.myCompany.gymBro.service.AuthService;
import com.myCompany.gymBro.service.dto.LoginDTO;
import com.myCompany.gymBro.web.config.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDTO loginDTO) {

        try {
            String jwt = authService.authenticateAndGenerateToken(loginDTO.getEmail(), loginDTO.getPassword());
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build();
        } catch (AuthenticationException e) {
            System.err.println("Error de autenticación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
