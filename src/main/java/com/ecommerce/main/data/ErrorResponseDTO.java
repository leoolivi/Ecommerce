package com.ecommerce.main.data;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public record ErrorResponseDTO(
        HttpStatus error,
        String message,
        int status,
        String path,
        LocalDateTime timestamp
) {}
