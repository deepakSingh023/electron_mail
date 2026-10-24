package com.example.mail.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${spring.jwt.secret}")
    private String secret;

    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private final Long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7;


    public String createToken(String userId , String email){
        return Jwts.builder()
                .subject(userId)
                .claim("email",email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.ES256)
                .compact();
    }

    public String extractUserId(String token) {
        try {

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();

        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token has expired");
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token");
        }
    }


    public boolean isTokenValid(String token) {
        try {
            extractUserId(token); // will throw if invalid/expired
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
