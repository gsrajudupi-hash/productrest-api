package com.ltm.training.producthive.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/**
 * This is the central configuration that replaces the default Spring Security login page 
 * and defines the API access rules.
 * 
 * csrf.disable()	Appropriate for this stateless bearer-token API; 
 * reassess if using cookie-based authentication
 * STATELESS	Prevents session-based authentication
 * PasswordEncoder	Verifies BCrypt password hashes
 * permitAll()	Allows registration and login without a JWT
 * GET /api/products/**	Allows product listing, search and retrieval without a JWT
 * anyRequest().authenticated()	Protects every other endpoint
 * addFilterBefore()	Runs the JWT filter before Spring's username/password 
 * authentication filter
 * 
 * Product access: In this code, product reads are public, but product writes 
 * require authentication. 
 * If you want every product endpoint to require a JWT, remove the public GET matcher. 
 * The other rules remain the same.
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(
            JwtAuthenticationFilter jwtFilter,
            UserDetailsService userDetailsService) {

        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))

            .authenticationProvider(daoAuthenticationProvider())

            .authorizeHttpRequests(auth -> auth

                // Public registration and login
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/register",
                    "/api/auth/login"
                ).permitAll()

//                // Public product listing, retrieval and search
//               .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
//               .requestMatchers(HttpMethod.GET, "/api/search/**").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/products/**").permitAll()
//                .requestMatchers(HttpMethod.PUT, "/api/products/**").permitAll()
//                .requestMatchers(HttpMethod.DELETE, "/api/products/**").permitAll()

                // Protect all remaining endpoints
                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
