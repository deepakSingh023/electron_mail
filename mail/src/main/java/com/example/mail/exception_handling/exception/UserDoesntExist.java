package com.example.mail.exception_handling.exception;

public class UserDoesntExist extends RuntimeException {
    public UserDoesntExist(String message) {
        super(message);
    }
}
