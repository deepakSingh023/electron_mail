package com.example.mail.controller;


import com.example.mail.dto.PublicKey;
import com.example.mail.dto.SendMail;
import com.example.mail.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/request")
public class MailController {

    private final MailService mailService;



    @PostMapping("/send")
    public ResponseEntity<Void> sendMail(
            @Valid @RequestBody SendMail mail,
            Authentication authentication
            ){

        String userId = authentication.getName();

        mailService.sendMail(mail,userId);

        return ResponseEntity.accepted().build();
    }

    @GetMapping("get_key")
    public ResponseEntity<PublicKey> getPublicKey(
            @RequestParam String email
    ){

        PublicKey key = mailService.getPublicKey(email);

        return ResponseEntity.ok(key);
    }
}
