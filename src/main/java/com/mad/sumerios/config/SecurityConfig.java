package com.mad.sumerios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/**").permitAll() // Permite acceso sin autenticación a todas las rutas que comienzan con /api/
                                .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
                )
                .csrf(csrf -> csrf.disable()); // Desactiva la protección CSRF (útil para pruebas)

        return http.build();
    }
}



