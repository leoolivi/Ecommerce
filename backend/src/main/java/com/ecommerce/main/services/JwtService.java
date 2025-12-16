package com.ecommerce.main.services;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T>T extractClaim(String token, Function<Claims, T> resolverFunction) {
        Claims claims = extractAllClaims(token);
        return resolverFunction.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    public SecretKey getSigningKey() {
        byte[] bytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(bytes);
    }

    public boolean validateToken(String token, UserDetails details) {
        String email = extractEmail(token);
        return email.equals(details.getUsername());
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails details) {
        return Jwts.builder()
            .claims(extraClaims)
            .subject(details.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis()+ jwtExpiration))
            .signWith(getSigningKey())
            .compact();
    }

    public String generateToken(UserDetails details) {
        return generateToken(null, details);
    }
}
