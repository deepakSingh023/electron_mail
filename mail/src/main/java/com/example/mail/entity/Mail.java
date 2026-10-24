package com.example.mail.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "mails")
public class Mail {

    @Id
    private String userId;

    private String receiverId;

    private String user_mail;

    private String receiver_mail;

    private String content;

    private String attachment;

    private Instant created_at;

}
