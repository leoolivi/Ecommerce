package com.ecommerce.main.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ecommerce.main.models.Product;
import com.ecommerce.main.repositories.AppUserRepository;
import com.ecommerce.main.services.ProductService;
import com.ecommerce.main.services.SettingService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final AppUserRepository appUserRepository;

    private final ProductService productService;
    private final SettingService settingService;

    ApplicationConfig(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Bean
    public UserDetailsService userDetailsService(String email, AppUserRepository repository) {
        return (email) -> {
            repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        };
    }
    
    @Bean
    public CommandLineRunner commandLineRunner() {
        return (String... args) -> {
            productService.addProduct(Product.builder()
                    .name("A qualcosa")
                    .description("Description")
                    .price( 18.99)
                    .stockQuantity(10)
                    .category("Category 1")
                    .imageUrl("")
                    .build());
            
            productService.addProduct(Product.builder()
                    .name("B qualcosaltro")
                    .description("Description 2")
                    .price(12.99)
                    .stockQuantity(5)
                    .category("Category 1")
                    .imageUrl("")
                    .build());

            settingService.addSetting("shipping_fee", "10");
        };
    }
}
