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

        @Size(min = 10 , max = 20)
        @NotBlank
        String password,

        @NotNull
        String private_key,

        @NotNull
        String public_key
) {
}
