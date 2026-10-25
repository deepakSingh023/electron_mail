package com.example.mail.exception_handling.exception;

public class WrongPassword extends RuntimeException {
    public WrongPassword(String message) {
        super(message);
    }
}
