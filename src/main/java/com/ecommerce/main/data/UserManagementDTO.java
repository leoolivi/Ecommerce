package com.ecommerce.main.data;

import com.ecommerce.main.models.enums.AppUserRole;

public record UserManagementDTO(
    String email,
    String password,
    AppUserRole role
) {}
    
