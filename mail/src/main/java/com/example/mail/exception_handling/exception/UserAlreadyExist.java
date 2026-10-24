package com.example.mail.exception_handling.exception;

public class UserAlreadyExist  extends RuntimeException{
    public UserAlreadyExist(String message){
        super (message);
    }
}
