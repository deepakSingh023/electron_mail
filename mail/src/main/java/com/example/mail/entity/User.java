package com.example.mail.entity;
import jakarta.persistence.*;
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
@Table(name = "users" ,
uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_users_username",
                columnNames = "username"
        ),
        @UniqueConstraint(
                name = "uk_users_email",
                columnNames = "email"
        )
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false , length = 50)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false , columnDefinition = "TEXT")
    private String public_key;

    @Column(nullable = false , columnDefinition = "TEXT")
    private String private_key;

    @Column(nullable = false)
    private Instant created_at;
}
