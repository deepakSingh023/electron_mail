package com.example.mail.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SesReceiptNotification(
        String notificationType,
        SesReceipt receipt,
        SesMail mail
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SesReceipt(
            List<String> recipients,
            SesAction action
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SesAction(
            String type,

            @JsonProperty("bucketName")
            String bucketName,

            @JsonProperty("objectKey")
            String objectKey
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SesMail(
            String messageId,
            String source,
            List<String> destination,
            SesHeaders commonHeaders
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SesHeaders(
            String subject,
            List<String> from,
            List<String> to,
            List<String> cc
    ) {}
}