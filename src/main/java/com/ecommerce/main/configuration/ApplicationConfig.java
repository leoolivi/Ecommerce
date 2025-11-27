package com.ecommerce.main.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecommerce.main.models.Product;
import com.ecommerce.main.services.ProductService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final ProductService productService;
    
    @Bean
    public CommandLineRunner commandLineRunner() {
        return (String... args) -> {
            productService.addProduct(Product.builder()
                    .name("A qualcosa")
                    .description("Description")
                    .price((float) 18.99)
                    .stockQuantity(10)
                    .category("Category 1")
                    .imageUrl("")
                    .build());
            
            productService.addProduct(Product.builder()
                    .name("B qualcosaltro")
                    .description("Description 2")
                    .price((float) 12.99)
                    .stockQuantity(5)
                    .category("Category 1")
                    .imageUrl("")
                    .build());
        };
    }
}
