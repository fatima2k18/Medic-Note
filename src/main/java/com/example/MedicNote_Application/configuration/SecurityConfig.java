package com.example.MedicNote_Application.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    @Autowired
    private UserDetailsService userDetailsService;


    // ✅ Register the UserDetailsService with authentication provider


    // ✅ Configure authentication manager
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // ✅ Spring Security 6+ style filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Swagger & Public Docs
                        .requestMatchers("/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Doctor Registration & Login (open)
                        .requestMatchers("/doctor/apis/register", "/doctor/apis/login").permitAll()

                        // Patient Registration (open)
                        .requestMatchers("/patient/apis/register").permitAll()

                        // View Doctors (public for patients)
                        .requestMatchers("/doctor/public/**").permitAll() // use endpoint like: /doctor/public/all

                        // Admin-only APIs
                        .requestMatchers("/doctor/apis/all").hasAnyRole("ADMIN", "PATIENT")  // All doctors list only for admin and patient

                        // Prescription
                        .requestMatchers("/prescriptions/create").hasRole("DOCTOR")
                        .requestMatchers("/prescriptions/all").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers("/prescriptions/**").authenticated() // to fetch by ID

                        // Patient APIs
                        .requestMatchers(HttpMethod.GET, "/patient/apis/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN") // allow GETs to doctors too
                        .requestMatchers(HttpMethod.PUT, "/patient/apis/**").hasRole("PATIENT")
                    //    .requestMatchers(HttpMethod.PUT, "/patient/apis/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/patient/apis/**").hasRole("ADMIN")

                        // Doctor APIs
                        .requestMatchers("/doctor/apis/register").permitAll()  // ✅ Allow register
                        .requestMatchers(HttpMethod.GET, "/doctor/apis/**").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/doctor/apis/**").hasRole("ADMIN")

                        // Allow Doctors to update or cancel prescriptions
                        .requestMatchers(HttpMethod.PUT, "/prescription/apis/**").hasRole("DOCTOR")

                      //   .requestMatchers(HttpMethod.PUT, "/doctor/apis/**").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/doctor/apis/**").permitAll()

                        // Any other request
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ✅ Use BCrypt for password encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
