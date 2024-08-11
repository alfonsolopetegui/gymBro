package com.myCompany.gymBro.web.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    private final Algorithm ALGORITHM;

    public JwtUtils(@Value("${jwt.secret}") String secretKey) {
        this.ALGORITHM = Algorithm.HMAC256(secretKey);
    }

    public String create(String email, String id) {
        return JWT.create()
                .withSubject(email)
                .withIssuer("Gymbro")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()  + TimeUnit.DAYS.toMillis(15)))
                .withClaim("userId", id)
                .sign(ALGORITHM);
    }

    public boolean isValid(String jwt) {
        try {
            JWT.require(ALGORITHM)
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUsername(String jwt) {
        return JWT.require(ALGORITHM)
                .build()
                .verify(jwt)
                .getSubject();
    }

    public String extractUserId(String token) {
        DecodedJWT decodedJWT = JWT.require(ALGORITHM)
                .withIssuer("Gymbro")
                .build()
                .verify(token);

        return decodedJWT.getClaim("userId").asString();
    }



}
