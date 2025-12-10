package com.ecommerce.main.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ecommerce.main.repositories.AppUserRepository;
import com.ecommerce.main.services.ProductService;
import com.ecommerce.main.services.SettingService;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class ApplicationConfig {

    private final SettingService settingService;

    @Bean
    public UserDetailsService userDetailsService(String email, AppUserRepository repository) {
        return (userEmail) -> {
            return repository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        };
    }
    
    @Bean
    public CommandLineRunner commandLineRunner() {
        return (String... args) -> {
            settingService.addSetting("shipping_fee", "10");
        };
    }
}
