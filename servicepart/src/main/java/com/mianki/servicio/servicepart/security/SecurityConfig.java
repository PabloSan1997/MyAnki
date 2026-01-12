package com.mianki.servicio.servicepart.security;

import com.mianki.servicio.servicepart.models.dtos.TokenTimes;
import com.mianki.servicio.servicepart.security.filter.JwtValidationFilter;
import com.mianki.servicio.servicepart.service.InitService;
import com.mianki.servicio.servicepart.service.JwtAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Value("${server.port}")
    private String port;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAccessService jwtAccessService) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a -> a
                        .requestMatchers(HttpMethod.POST,
                                "/api/user/login",
                                "/api/user/register",
                                "/api/user/logout",
                                "/api/user/refresh",
                                "/api/user/socket"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/user/userinfo"
                        ).hasRole("USER")
                        .requestMatchers(
                                "/api/notes", "/api/notes/**"
                        ).hasRole("USER")
                        .anyRequest().authenticated()
                )
                .addFilter(new JwtValidationFilter(authenticationManager(), jwtAccessService))
                .cors(c-> c.configurationSource(corsConfigurationSource()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    TokenTimes tokentimes() {
        return new TokenTimes(60*7, 60*60*24);
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // Specific origins
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(List.of("*")); // Or specific headers
        configuration.setAllowCredentials(true); // If sending cookies/auth

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all paths
        return source;
    }

    @Bean
    CommandLineRunner commandLineRunner(InitService initService) {
        return args -> {
            initService.init();
            System.out.println("\nPort: " + port + "\n");
        };
    }
}
