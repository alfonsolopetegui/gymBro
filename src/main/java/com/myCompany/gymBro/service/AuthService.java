package com.myCompany.gymBro.service;

import com.myCompany.gymBro.web.config.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public String authenticateAndGenerateToken(String email, String password) {
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = authenticationManager.authenticate(login);

        if (authentication.isAuthenticated()) {
            // Obtener el userId usando el email
            String userId = String.valueOf(userService.findByEmail(email).getUserId());

            // Generar el token con el userId
            return jwtUtils.create(email, userId);
        } else {
            throw new AuthenticationException("Autenticación fallida") {};
        }
    }
}
