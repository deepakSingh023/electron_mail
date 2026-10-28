package com.example.mail.service;

import com.example.mail.dto.SnsNotification;

public interface SesInboundNotificationService {

    void handle(SnsNotification notification);
}
