package com.ecommerce.main.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.main.data.AuthResponseDTO;
import com.ecommerce.main.data.LoginRequestDTO;
import com.ecommerce.main.data.RegisterRequestDTO;
import com.ecommerce.main.data.UserResponseDTO;
import com.ecommerce.main.exceptions.InvalidCredentialsException;
import com.ecommerce.main.models.AppUser;
import com.ecommerce.main.models.enums.AppUserRole;
import com.ecommerce.main.repositories.AppUserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {
    
    private final UserDetailsService userDetailsService;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponseDTO login(LoginRequestDTO request) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        if (auth !=null && auth.isAuthenticated()) {
            AppUser details = (AppUser) userDetailsService.loadUserByUsername(request.email());
            String token = jwtService.generateToken(details);
            return new AuthResponseDTO(token, (long) 3600, new UserResponseDTO(details.getId(), details.getUsername(), details.getRole()));
        }
        throw new InvalidCredentialsException();
    }

    public AuthResponseDTO register(RegisterRequestDTO request) {
        // if(userDetailsService.loadUserByUsername(request.email()) != null) throw new EmailAlreadyExistsException(request.email());

        AppUser newUser = AppUser.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .role(AppUserRole.CUSTOMER)
            .build();
        newUser = userRepository.save(newUser);
        String token = jwtService.generateToken(newUser);
        return new AuthResponseDTO(token, (long) 3600, new UserResponseDTO(newUser.getId(), newUser.getUsername(), newUser.getRole()));
    }
}
