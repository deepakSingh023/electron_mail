package com.example.mail;


import com.example.mail.dto.SendMail;
import com.example.mail.entity.Mail;
import com.example.mail.entity.User;
import com.example.mail.repository.MailRepository;
import com.example.mail.repository.UserRepository;
import com.example.mail.service.MailServiceImpl;
import com.example.mail.service.SmtpMailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MailServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MailRepository mailRepository;

    @Mock
    private SmtpMailService smtpMailService;

    @InjectMocks
    private MailServiceImpl mailService;

    @Test
    void sendMail_externalRecipient_callsSmtp() {
        User sender = User.builder()
                .id("sender-1")
                .email("alice@electronmail.com")
                .build();

        SendMail data = new SendMail(
                "bob@gmail.com",
                null,
                "Hello",
                "Test message",
                List.of()
        );

        when(userRepository.findById("sender-1"))
                .thenReturn(Optional.of(sender));

        when(mailRepository.save(any(Mail.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mailService.sendMail(data, "sender-1");

        verify(mailRepository).save(any(Mail.class));

        verify(smtpMailService).send(
                data,
                "sender-1",
                true,
                false
        );
    }


}
