package com.myCompany.gymBro.service;


import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.myCompany.gymBro.exception.UserNotFoundException;
import com.myCompany.gymBro.persistence.entity.GoogleTokenEntity;
import com.myCompany.gymBro.persistence.entity.UserEntity;
import com.myCompany.gymBro.persistence.repository.GoogleTokenRepository;
import com.myCompany.gymBro.persistence.repository.UserRepository;
import com.myCompany.gymBro.web.config.GoogleApiProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class GoogleTokenService {

    private final GoogleTokenRepository googleTokenRepository;
    private final UserRepository userRepository;
    private final GoogleApiProperties googleApiProperties;

    public GoogleTokenService(GoogleTokenRepository googleTokenRepository, UserRepository userRepository, GoogleApiProperties googleApiProperties) {
        this.googleTokenRepository = googleTokenRepository;
        this.userRepository = userRepository;
        this.googleApiProperties = googleApiProperties;
    }

    // Método para manejar el intercambio de código y guardar el token
    public void handleOAuth2Callback(String code, String userId) {
        try {
            // Intercambia el código por un token de acceso
            Map<String, String> tokenResponse = exchangeCodeForAccessToken(code);

            // Extrae los detalles del token de la respuesta
            String accessToken = tokenResponse.get("access_token");
            String refreshToken = tokenResponse.get("refresh_token"); // Asegúrate de que esto esté presente
            long expiresInSeconds = Long.parseLong(tokenResponse.get("expires_in"));
            LocalDateTime expiresIn = LocalDateTime.now().plusSeconds(expiresInSeconds);

            // Busca el usuario por su ID
            UserEntity user = userRepository.findById(UUID.fromString(userId))
                    .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

            // Busca el token en la base de datos
            GoogleTokenEntity googleTokenEntity = googleTokenRepository.findByUser_UserId(user.getUserId())
                    .orElse(new GoogleTokenEntity()); // Crea un nuevo objeto si no existe

            // Actualiza el token y otros detalles
            googleTokenEntity.setUser(user);
            googleTokenEntity.setAccessToken(accessToken);
            googleTokenEntity.setExpiresIn(expiresIn);
            googleTokenEntity.setRefreshToken(refreshToken);

            // Guarda el token (inserción o actualización)
            googleTokenRepository.save(googleTokenEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error al manejar la respuesta de OAuth2: " + e.getMessage());
        }
    }


    // Método para intercambiar el código por un token de acceso
    private Map<String, String> exchangeCodeForAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = googleApiProperties.getTokenEndpoint();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        String requestBody = String.format(
                "code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code",
                code,
                googleApiProperties.getId(),
                googleApiProperties.getSecret(),
                googleApiProperties.getRedirectUri()
        );

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {
                }
        );

        Map<String, String> responseBody = response.getBody();
        System.out.println("Token Response: " + responseBody);

        return responseBody;
    }

    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Elimina el prefijo "Bearer "
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

    public boolean isTokenValid(String accessToken) {
        try {
            // Llama a un endpoint de la API de Google para validar el token
            String url = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=" + accessToken;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            return responseCode == 200; // Si el código de respuesta es 200, el token es válido
        } catch (IOException e) {
            System.err.println("Error al verificar el token: " + e.getMessage());
            return false;
        }
    }

    public String refreshToken(String refreshToken) {
        try {
            GoogleRefreshTokenRequest request = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    refreshToken,
                    googleApiProperties.getId(),
                    googleApiProperties.getSecret()
            );
            return request.execute().getAccessToken();
        } catch (IOException e) {
            throw new RuntimeException("Error al renovar el token de acceso", e);
        }
    }


}





