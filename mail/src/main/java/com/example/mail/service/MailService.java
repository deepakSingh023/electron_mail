package com.example.mail.service;

import com.example.mail.dto.PublicKey;
import com.example.mail.dto.SendMail;

public interface MailService {

    void sendMail(SendMail data , String senderId);

    PublicKey getPublicKey(String email);
}
