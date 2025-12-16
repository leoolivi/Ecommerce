package com.ecommerce.main.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecommerce.main.data.ErrorResponseDTO;
import com.ecommerce.main.exceptions.InvalidCredentialsException;
import com.ecommerce.main.services.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService detailsService;

    private void handleJwtError(HttpServletResponse response, String message, HttpServletRequest request) throws IOException {
        ErrorResponseDTO errorBody = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED,
                message,
                HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        
        ObjectMapper mapper = new ObjectMapper();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(mapper.writeValueAsString(errorBody));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getServletPath().startsWith("/api/v1/auth")) {
            log.info("Filter bypassed (auth endpoints)");
            filterChain.doFilter(request, response);
        }
        
        String header = request.getHeader("Authorization");
        try {
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                String email = jwtService.extractEmail(token);
                UserDetails details = detailsService.loadUserByUsername(email);
                if (email != null && jwtService.validateToken(token, details)) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
                    auth.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    throw new InvalidCredentialsException();
                }

            }

            filterChain.doFilter(request, response);
        } catch (SignatureException ex) {
            handleJwtError(response, "Invalid token signature", request);
        } catch (ExpiredJwtException ex) {
            handleJwtError(response, "Token expired", request);
        } catch (JwtException ex) {
            handleJwtError(response, "Malformed or invalid token", request);
        }
    }
    
}
