package com.example.mail.dto;

import java.util.List;

public record ParsedInboundEmail(
        String senderEmail,
        String subject,
        String textBody,
        String htmlBody,
        List<ParsedAttachment> attachments
) {
    public record ParsedAttachment(
            String filename,
            String contentType,
            byte[] content
    ) {}
}