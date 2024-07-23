package com.myCompany.gymBro.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                //desactivando la protección CSRF (Cross-Site Request Forgery)
                // es una característica de seguridad que ayuda a prevenir ciertos tipos de ataques, pero en algunos casos, puedes querer desactivarla
                .csrf(AbstractHttpConfigurer::disable)
                //habilitando la configuración de CORS (Cross-Origin Resource Sharing)
                .cors(Customizer.withDefaults())
                //Politica de creación de sesión
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Personalizacion de la configuración de seguridad
                .authorizeHttpRequests(customRequest -> {
                    customRequest
                            .requestMatchers("/api/**").permitAll()
                            .anyRequest().permitAll();
                })
                //habilitando la autenticación básica HTTP
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }


    //Cren un codificador de contraseñas, en esto BCRYPT
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
