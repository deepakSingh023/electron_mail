package com.example.mail.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "mails")
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = true)
    private String userId;

    private String receiverId;

    private String ccId;

    @Column(nullable = false)
    private String user_mail;

    @Column(nullable = false)
    private String receiver_mail;

    private String ccMail;

    private String subject;

    private String content;

    @ElementCollection
    @CollectionTable(
            name = "mail_attachments",
            joinColumns = @JoinColumn(name = "mail_id")
    )
    @Column(name = "attachment_id")
    private List<String> attachment;

    @Column(nullable = false)
    private Instant created_at;

}
