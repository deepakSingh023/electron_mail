package com.example.mail.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain filterChain
    ) throws IOException, ServletException {

        // Refresh tokens are handled by the refresh endpoint/service.
        if ("/api/auth/refresh".equals(req.getServletPath())) {
            filterChain.doFilter(req, res);
            return;
        }

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(req, res);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.extractClaims(token);

            String tokenType = claims.get("token_type", String.class);

            // Only access tokens can authenticate normal API requests.
            if (!"access".equals(tokenType)) {
                res.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Access token required"
                );
                return;
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            claims.getSubject(),
                            null,
                            Collections.emptyList()
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

        } catch (JwtException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
            res.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid or expired access token"
            );
            return;
        }

        filterChain.doFilter(req, res);
    }
}