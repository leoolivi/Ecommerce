// ========================================
// JwtServiceTest.java
// ========================================
package com.ecommerce.main.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.ecommerce.main.models.AppUser;
import com.ecommerce.main.models.enums.AppUserRole;

import io.jsonwebtoken.Claims;

@DisplayName("JWT Service Tests")
class JwtServiceTest {

    private JwtService jwtService;
    private AppUser testUser;
    private AppUser wrongUser;
    private String jwtSecret = "90facd6fc7f53e42a746fc1e4f0dea78adcab881db62815cd3812455d6c9226f";
    private int jwtExpiration = 3600000;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", jwtExpiration);

        testUser = AppUser.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .role(AppUserRole.CUSTOMER)
                .build();

        wrongUser = AppUser.builder()
                .id(2L)
                .email("wrong@example.com")
                .password("password")
                .role(AppUserRole.CUSTOMER)
                .build();
    }

    @Test
    @DisplayName("Should generate valid JWT token")
    void testGenerateToken() {
        // Act
        String token = jwtService.generateToken(testUser);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    @DisplayName("Should extract email from token")
    void testExtractEmail() {
        // Arrange
        String token = jwtService.generateToken(testUser);

        // Act
        String email = jwtService.extractEmail(token);

        // Assert
        assertEquals("test@example.com", email);
    }

    @Test
    @DisplayName("Should extract all claims from token")
    void testExtractAllClaims() {
        // Arrange
        String token = jwtService.generateToken(testUser);

        // Act
        Claims claims = jwtService.extractAllClaims(token);

        // Assert
        assertNotNull(claims);
        assertEquals("test@example.com", claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    @DisplayName("Should validate token successfully")
    void testValidateToken() {
        // Arrange
        String token = jwtService.generateToken(testUser);

        // Act
        boolean isValid = jwtService.validateToken(token, testUser);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should invalidate token with wrong user")
    void testValidateTokenWrongUser() {
        // Arrange
        String token = jwtService.generateToken(testUser);

        // Act
        boolean isValid = jwtService.validateToken(token, wrongUser);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should generate different tokens for same user")
    void testGenerateDifferentTokens() {
        // Act
        String token1 = jwtService.generateToken(testUser);
        String token2 = jwtService.generateToken(testUser);

        // Assert
        assertNotEquals(token1, token2); // Due to different issued timestamps
    }

    @Test
    @DisplayName("Should extract claims using custom resolver")
    void testExtractClaimWithResolver() {
        // Arrange
        String token = jwtService.generateToken(testUser);

        // Act
        String subject = jwtService.extractClaim(token, Claims::getSubject);

        // Assert
        assertEquals("test@example.com", subject);
    }

    @Test
    @DisplayName("Should get signing key")
    void testGetSigningKey() {
        // Act
        var key = jwtService.getSigningKey();

        // Assert
        assertNotNull(key);
        assertEquals("HmacSHA256", key.getAlgorithm());
    }
}