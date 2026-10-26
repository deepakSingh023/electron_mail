package com.example.mail.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record SendMail(
        @NotBlank
        @Email
        String recipientEmail,

        @Email
        String ccMail,

        @NotBlank
        String subject,

        @NotBlank
        String content,

        List<String> attachmentIds
) {}