package com.ecommerce.main.data;

import com.ecommerce.main.models.enums.AppUserRole;

public record UserResponseDTO(
        Long id,
        String email,
        AppUserRole role
) {}