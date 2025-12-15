package com.ecommerce.main.data;

public record PasswordResetCheckDTO(
    char[] otp,
    String newPassword
) {}
