package com.example.mail.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${spring.jwt.secret}")
    private String secret;

    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private static final long ACCESS_EXPIRATION =
            1000L * 60 * 15; // 15 minutes

    private static final long REFRESH_EXPIRATION =
            1000L * 60 * 60 * 24 * 7; // 7 days


    public String createAccessToken(String userId , String email){
        return Jwts.builder()
                .subject(userId)
                .claim("email",email)
                .claim("token_type","access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String createRefreshToken(String userId ){
        return Jwts.builder()
                .subject(userId)
                .claim("token_type","refresh")
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
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
