package com.example.mail.service;
import com.example.mail.dto.ParsedInboundEmail;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class MimeEmailParser {

    public ParsedInboundEmail parse(MimeMessage message)
            throws MessagingException, IOException {

        String sender = message.getFrom() != null
                && message.getFrom().length > 0
                ? message.getFrom()[0].toString()
                : null;

        String subject = message.getSubject();

        List<ParsedInboundEmail.ParsedAttachment> attachments =
                new ArrayList<>();

        StringBuilder textBody = new StringBuilder();
        StringBuilder htmlBody = new StringBuilder();

        extractParts(
                message,
                attachments,
                textBody,
                htmlBody
        );

        return new ParsedInboundEmail(
                sender,
                subject,
                textBody.toString(),
                htmlBody.toString(),
                attachments
        );
    }

    private void extractParts(
            Part part,
            List<ParsedInboundEmail.ParsedAttachment> attachments,
            StringBuilder textBody,
            StringBuilder htmlBody
    ) throws MessagingException, IOException {

        String disposition = part.getDisposition();
        String filename = part.getFileName();

        // Attachments can be identified by disposition or filename.
        if (Part.ATTACHMENT.equalsIgnoreCase(disposition)
                || filename != null) {

            String decodedFilename = filename == null
                    ? "attachment"
                    : MimeUtility.decodeText(filename);

            byte[] bytes;

            try (InputStream input = part.getInputStream();
                 ByteArrayOutputStream output =
                         new ByteArrayOutputStream()) {

                input.transferTo(output);
                bytes = output.toByteArray();
            }

            attachments.add(
                    new ParsedInboundEmail.ParsedAttachment(
                            decodedFilename,
                            part.getContentType(),
                            bytes
                    )
            );

            return;
        }

        if (part.isMimeType("text/plain")) {
            Object content = part.getContent();

            if (content instanceof String text) {
                textBody.append(text);
            }

            return;
        }

        if (part.isMimeType("text/html")) {
            Object content = part.getContent();

            if (content instanceof String html) {
                htmlBody.append(html);
            }

            return;
        }

        if (part.isMimeType("multipart/*")) {
            Object content = part.getContent();

            if (content instanceof Multipart multipart) {
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);

                    extractParts(
                            bodyPart,
                            attachments,
                            textBody,
                            htmlBody
                    );
                }
            }
        }
    }
}