package com.ecommerce.main.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "otp_codes")
@NoArgsConstructor  // Per JPA
@AllArgsConstructor // Per avere tutti i campi se serve
@Getter @Setter
public class OTPCode {
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private char[] code;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private String userEmail;
    private Long userId;
    
    // Factory method statico - nessuna dependency injection
    public static OTPCode create(char[] code, String userEmail, Long userId) {
        OTPCode otp = new OTPCode();
        otp.code = code;
        otp.userEmail = userEmail;
        otp.userId = userId;
        otp.issuedAt = LocalDateTime.now();
        otp.expiresAt = LocalDateTime.now().plusMinutes(10);
        return otp;
    }
    
    // Factory method con durata personalizzata
    public static OTPCode create(char[] code, String userEmail, Long userId, int expirationMinutes) {
        OTPCode otp = new OTPCode();
        otp.code = code;
        otp.userEmail = userEmail;
        otp.userId = userId;
        otp.issuedAt = LocalDateTime.now();
        otp.expiresAt = LocalDateTime.now().plusMinutes(expirationMinutes);
        return otp;
    }
    
    // Metodo helper per verificare scadenza
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}