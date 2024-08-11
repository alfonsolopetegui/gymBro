package com.myCompany.gymBro.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

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
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/classes/save").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/classes/update").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/classes/delete/{classId}").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/api/schedules/save").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/schedules/update").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/schedules/delete/{scheduleId}").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/api/subscriptions/save").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/subscriptions/update").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/subscriptions/delete/{subscriptionId}").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/api/users/summary").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/api/users/save").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/users/update").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/users/delete/{userId}").hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                //habilitando la autenticación jwt
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    //Creo un codificador de contraseñas, BCRYPT
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
