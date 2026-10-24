package com.example.mail.dto;

public record ExceptionMessage(
        String Status,
        String error,
        String message,
        String timestamp
) {
}
