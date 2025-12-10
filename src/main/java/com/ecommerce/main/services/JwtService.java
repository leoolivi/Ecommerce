package com.ecommerce.main.services;

import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private final String jwtSecret = "";

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T>T extractClaim(String token, Function<Claims, T> resolverFunction) {
        Claims claims = extractAllClaims(token);
        return resolverFunction.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().decryptWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    public SecretKey getSigningKey() {
        byte[] bytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(bytes);
    }
}
