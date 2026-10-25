package com.example.mail.dto;

public record RegisterResponse(

        String id,
        String accessToken,
        String refreshToken

) {
}
