package com.ecommerce.main.data;

public record AuthResponseDTO(
        String accessToken,
        Long expiresIn,
        UserResponseDTO user
) {}