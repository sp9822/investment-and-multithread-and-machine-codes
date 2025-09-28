package org.example.invest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the application
 * This configuration allows public access to stocks API endpoints
 * while maintaining security for other endpoints
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API endpoints
            .authorizeHttpRequests(authz -> authz
                // Allow public access to stocks API endpoints
                .requestMatchers("/api/stocks/**").permitAll()
                // Allow public access to Swagger/OpenAPI documentation
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                // Allow public access to health check endpoints
                .requestMatchers("/actuator/**").permitAll()
                // Require authentication for all other endpoints
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> httpBasic.disable()) // Disable basic auth
            .formLogin(formLogin -> formLogin.disable()); // Disable form login

        return http.build();
    }
}

