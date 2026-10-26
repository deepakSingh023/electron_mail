package com.example.mail.service;

import com.example.mail.dto.SendMail;
import com.example.mail.entity.User;
import com.example.mail.exception_handling.exception.UserDoesntExist;
import com.example.mail.repository.UserRepository;
import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SmtpMailService {

    private final JavaMailSenderImpl javaMailSender;
    private final UserRepository userRepository;

    public void send(
            SendMail data,
            String senderId,
            boolean sendToRecipient,
            boolean sendToCc
    ) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() ->
                        new UserDoesntExist("Sender not found"));

        List<Address> envelopeRecipients = new ArrayList<>();

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setFrom(sender.getEmail());

            helper.setTo(data.recipientEmail());

            if (data.ccMail() != null && !data.ccMail().isBlank()) {
                helper.setCc(data.ccMail());
            }

            helper.setSubject(data.subject());
            helper.setText(data.content());


            if (sendToRecipient) {
                envelopeRecipients.add(
                        new InternetAddress(data.recipientEmail())
                );
            }

            if (sendToCc
                    && data.ccMail() != null
                    && !data.ccMail().isBlank()) {
                envelopeRecipients.add(
                        new InternetAddress(data.ccMail())
                );
            }

            if (envelopeRecipients.isEmpty()) {
                return;
            }

            Transport transport = null;

            try {
                transport = javaMailSender.getSession()
                        .getTransport("smtp");

                transport.connect(
                        javaMailSender.getHost(),
                        javaMailSender.getPort(),
                        javaMailSender.getUsername(),
                        javaMailSender.getPassword()
                );

                transport.sendMessage(
                        message,
                        envelopeRecipients.toArray(new Address[0])
                );
            } finally {
                if (transport != null && transport.isConnected()) {
                    transport.close();
                }
            }

        } catch (MessagingException e) {
            throw new MailSendException(
                    "Failed to send email through SMTP", e
            );
        }
    }
}
