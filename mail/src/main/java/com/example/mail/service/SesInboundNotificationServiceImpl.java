package com.example.mail.service;

import com.example.mail.dto.ParsedInboundEmail;
import com.example.mail.dto.SesReceiptNotification;
import com.example.mail.dto.SnsNotification;

import software.amazon.awssdk.thirdparty.jackson.core.JsonProcessingException;
import tools.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class SesInboundNotificationServiceImpl
        implements SesInboundNotificationService {

    private final ObjectMapper objectMapper;
    private final S3Client s3Client;
    private final MimeEmailParser mimeEmailParser;

    @Override
    public void handle(SnsNotification notification) {

        if (!"Notification".equals(notification.type())) {
            return;
        }

        try {
            SesReceiptNotification sesNotification =
                    objectMapper.readValue(
                            notification.message(),
                            SesReceiptNotification.class
                    );

            String bucket = sesNotification.receipt()
                    .action().bucketName();

            String objectKey = sesNotification.receipt()
                    .action().objectKey();

            byte[] rawEmail = s3Client.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(objectKey)
                            .build()
            ).asByteArray();

            Session session = Session.getInstance(new Properties());

            MimeMessage message = new MimeMessage(
                    session,
                    new ByteArrayInputStream(rawEmail)
            );

            ParsedInboundEmail parsed = mimeEmailParser.parse(message);

            String sender = parsed.senderEmail();
            String subject = parsed.subject();
            String textBody = parsed.textBody();
            String htmlBody = parsed.htmlBody();

            List<ParsedInboundEmail.ParsedAttachment> attachments =
                    parsed.attachments();

        }catch (MessagingException | IOException e) {
            throw new IllegalStateException(
                    "Failed to process inbound email", e
            );
        }
    }
}