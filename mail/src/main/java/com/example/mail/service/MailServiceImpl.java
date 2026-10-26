package com.example.mail.service;


import com.example.mail.dto.PublicKey;
import com.example.mail.dto.SendMail;
import com.example.mail.entity.Mail;
import com.example.mail.entity.User;
import com.example.mail.exception_handling.exception.UserDoesntExist;
import com.example.mail.repository.MailRepository;
import com.example.mail.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final UserRepository userRepository;
    private final MailRepository mailRepository;
    private final SmtpMailService smtpMailService;

    @Override
    public void sendMail(SendMail data, String senderId) {

        User sender = userRepository.findById(senderId)
                .orElseThrow(() ->
                        new UserDoesntExist("Sender not found"));

        User receiver = findInternalUser(data.recipientEmail());
        User ccUser = data.ccMail() == null || data.ccMail().isBlank()
                ? null
                : findInternalUser(data.ccMail());

        Mail mail = Mail.builder()
                .userId(sender.getId())
                .user_mail(sender.getEmail())
                .receiverId(receiver != null ? receiver.getId() : null)
                .receiver_mail(data.recipientEmail())
                .ccId(ccUser != null ? ccUser.getId() : null)
                .ccMail(data.ccMail())
                .subject(data.subject())
                .content(data.content())
                .attachment(data.attachmentIds())
                .created_at(Instant.now())
                .build();

        mailRepository.save(mail);


        boolean externalTo = receiver == null;

        boolean externalCc = data.ccMail() != null
                && !data.ccMail().isBlank()
                && ccUser == null;


        if (externalTo || externalCc) {
            smtpMailService.send(
                    data,
                    senderId,
                    externalTo,
                    externalCc
            );
        }
    }

    private User findInternalUser(String email) {
        if (email == null || !email.toLowerCase(Locale.ROOT)
                .endsWith("@electronmail.com")) {
            return null;
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserDoesntExist("Internal recipient not found"));
    }


    @Override
    public PublicKey getPublicKey(String email){

        User receiver = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDoesntExist("Receiver doesn't exist"));

        return new PublicKey(receiver.getPublic_key());

    }



}
