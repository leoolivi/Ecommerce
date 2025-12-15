package com.ecommerce.main.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecommerce.main.repositories.AppUserRepository;
import com.ecommerce.main.services.SettingService;
import com.ecommerce.main.utility.EmailUtility;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class ApplicationConfig {

    private final SettingService settingService;
    private final AppUserRepository repository;
    private final EmailUtility emailUtility;

    @Bean
    public UserDetailsService userDetailsService(AppUserRepository repository) {
        return (userEmail) -> {
            return repository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        };
    }
    
    @Bean
    public CommandLineRunner commandLineRunner() {
        return (String... args) -> {
            settingService.addSetting("shipping_fee", "10");
            // emailUtility.sendEmail("leolivieri1910@gmail.com", "TEST", "prova da ecommerce");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService(repository));
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
