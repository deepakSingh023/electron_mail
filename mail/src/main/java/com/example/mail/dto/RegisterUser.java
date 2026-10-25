package com.example.mail.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterUser(

        @NotBlank
        @Size(max = 50)
        String username,

        @Email
        String email,

        @Size(min = 12 , max = 72)
        @NotBlank
        String password,

        @NotBlank
        String private_key,

        @NotBlank
        String public_key
) {
}
