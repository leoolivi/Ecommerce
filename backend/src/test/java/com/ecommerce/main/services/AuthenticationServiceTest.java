// ========================================
// AuthenticationServiceTest.java
// ========================================
package com.ecommerce.main.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecommerce.main.data.AuthResponseDTO;
import com.ecommerce.main.data.LoginRequestDTO;
import com.ecommerce.main.data.RegisterRequestDTO;
import com.ecommerce.main.exceptions.InvalidCredentialsException;
import com.ecommerce.main.models.AppUser;
import com.ecommerce.main.models.enums.AppUserRole;
import com.ecommerce.main.repositories.AppUserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Authentication Service Tests")
class AuthenticationServiceTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthenticationService authenticationService;

    private AppUser testUser;
    private LoginRequestDTO loginRequest;
    private RegisterRequestDTO registerRequest;

    @BeforeEach
    void setUp() {
        testUser = AppUser.builder()
                .id(1L)
                .email("test@example.com")
                .password("$2a$12$encodedPassword")
                .role(AppUserRole.CUSTOMER)
                .build();

        loginRequest = new LoginRequestDTO("test@example.com", "password");
        registerRequest = new RegisterRequestDTO("new@example.com", "password");
    }

    @Test
    @DisplayName("Should login successfully with valid credentials")
    void testLoginSuccess() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(testUser);
        when(jwtService.generateToken(testUser)).thenReturn("jwt.token.here");

        // Act
        AuthResponseDTO response = authenticationService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt.token.here", response.accessToken());
        assertEquals(3600L, response.expiresIn());
        assertEquals("test@example.com", response.user().email());
        assertEquals(AppUserRole.CUSTOMER, response.user().role());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtService, times(1)).generateToken(testUser);
    }

    @Test
    @DisplayName("Should throw exception with invalid credentials")
    void testLoginFailure() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            authenticationService.login(loginRequest);
        });
    }

    @Test
    @DisplayName("Should throw exception when authentication is null")
    void testLoginNullAuthentication() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            authenticationService.login(loginRequest);
        });
    }

    @Test
    @DisplayName("Should register new user successfully")
    void testRegisterSuccess() {
        // Arrange
        AppUser newUser = AppUser.builder()
                .id(2L)
                .email("new@example.com")
                .password("$2a$12$encodedPassword")
                .role(AppUserRole.CUSTOMER)
                .build();

        when(passwordEncoder.encode("password")).thenReturn("$2a$12$encodedPassword");
        when(userRepository.save(any(AppUser.class))).thenReturn(newUser);
        when(jwtService.generateToken(any(AppUser.class))).thenReturn("jwt.token.here");

        // Act
        AuthResponseDTO response = authenticationService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt.token.here", response.accessToken());
        assertEquals(3600L, response.expiresIn());
        assertEquals("new@example.com", response.user().email());
        assertEquals(AppUserRole.CUSTOMER, response.user().role());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(AppUser.class));
        verify(jwtService, times(1)).generateToken(any(AppUser.class));
    }

    @Test
    @DisplayName("Should assign CUSTOMER role to new user")
    void testRegisterAssignsCustomerRole() {
        // Arrange
        AppUser newUser = AppUser.builder()
                .id(2L)
                .email("new@example.com")
                .password("$2a$12$encodedPassword")
                .role(AppUserRole.CUSTOMER)
                .build();

        when(passwordEncoder.encode(anyString())).thenReturn("$2a$12$encodedPassword");
        when(userRepository.save(any(AppUser.class))).thenReturn(newUser);
        when(jwtService.generateToken(any(AppUser.class))).thenReturn("jwt.token.here");

        // Act
        AuthResponseDTO response = authenticationService.register(registerRequest);

        // Assert
        assertEquals(AppUserRole.CUSTOMER, response.user().role());
    }

    @Test
    @DisplayName("Should encode password during registration")
    void testRegisterEncodesPassword() {
        // Arrange
        AppUser newUser = AppUser.builder()
                .id(2L)
                .email("new@example.com")
                .password("$2a$12$encodedPassword")
                .role(AppUserRole.CUSTOMER)
                .build();

        when(passwordEncoder.encode("password")).thenReturn("$2a$12$encodedPassword");
        when(userRepository.save(any(AppUser.class))).thenReturn(newUser);
        when(jwtService.generateToken(any(AppUser.class))).thenReturn("jwt.token.here");

        // Act
        authenticationService.register(registerRequest);

        // Assert
        verify(passwordEncoder, times(1)).encode("password");
    }
}
