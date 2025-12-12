package com.ecommerce.main.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecommerce.main.security.JwtFilter;

import lombok.AllArgsConstructor;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/orders/**").authenticated()
                .requestMatchers("/api/v1/admin/**").authenticated()
                .requestMatchers("/api/v1/products/**").authenticated().anyRequest().permitAll()
            )
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(Customizer.withDefaults())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
