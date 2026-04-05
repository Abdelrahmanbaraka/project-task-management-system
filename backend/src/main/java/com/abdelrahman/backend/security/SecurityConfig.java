package com.abdelrahman.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/me").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("ADMIN", "PROJECT_LEADER")

                        .requestMatchers(HttpMethod.POST, "/api/projects").hasRole("PROJECT_LEADER")
                        .requestMatchers(HttpMethod.PUT, "/api/projects/*").hasRole("PROJECT_LEADER")
                        .requestMatchers(HttpMethod.PUT, "/api/projects/*/archive").hasRole("PROJECT_LEADER")
                        .requestMatchers(HttpMethod.POST, "/api/projects/assign-member").hasRole("PROJECT_LEADER")
                        .requestMatchers(HttpMethod.GET, "/api/projects/*/progress").hasRole("PROJECT_LEADER")
                        .requestMatchers(HttpMethod.GET, "/api/projects/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/tasks").hasAnyRole("EMPLOYEE", "PROJECT_LEADER")
                        .requestMatchers(HttpMethod.PUT, "/api/tasks/*").hasAnyRole("EMPLOYEE", "PROJECT_LEADER")
                        .requestMatchers(HttpMethod.PATCH, "/api/tasks/*/status").hasAnyRole("EMPLOYEE", "PROJECT_LEADER")
                        .requestMatchers(HttpMethod.GET, "/api/tasks/**").authenticated()

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}