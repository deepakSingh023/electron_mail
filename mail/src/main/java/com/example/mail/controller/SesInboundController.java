package com.example.mail.controller;
import com.example.mail.dto.SnsNotification;
import com.example.mail.service.SesInboundNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inbound/ses")
@RequiredArgsConstructor
public class SesInboundController {

    private final SesInboundNotificationService notificationService;

    @PostMapping
    public ResponseEntity<Void> receive(
            @RequestBody SnsNotification notification) {

        notificationService.handle(notification);

        return ResponseEntity.ok().build();
    }
}