package com.example.mail.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SnsNotification(
        @JsonProperty("Type")
        String type,

        @JsonProperty("Message")
        String message,

        @JsonProperty("TopicArn")
        String topicArn,

        @JsonProperty("SubscribeURL")
        String subscribeUrl,

        @JsonProperty("Signature")
        String signature,

        @JsonProperty("SigningCertURL")
        String signingCertUrl
) {}