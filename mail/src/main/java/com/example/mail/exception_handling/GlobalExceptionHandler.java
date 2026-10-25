package com.example.mail.exception_handling;


import com.example.mail.dto.ExceptionMessage;
import com.example.mail.exception_handling.exception.UserAlreadyExist;
import com.example.mail.exception_handling.exception.UserDoesntExist;
import com.example.mail.exception_handling.exception.WrongPassword;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExist.class)
    public ResponseEntity<ExceptionMessage> handleUserAlreadyExist(UserAlreadyExist ex){

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                "409 CONFLICT",
                "Already exists",
                ex.getMessage(),
                Instant.now().toString()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionMessage);

    }

    @ExceptionHandler(UserDoesntExist.class)
    public ResponseEntity<ExceptionMessage> handleUserDoesntExist(UserDoesntExist ex){

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                "404 Not Found",
                "User doesnt exist",
                ex.getMessage(),
                Instant.now().toString()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionMessage);

    }

    @ExceptionHandler(WrongPassword.class)
    public ResponseEntity<ExceptionMessage> handleWrongPassword(WrongPassword ex){

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                "401 Unauthorized",
                "password provided is wrong",
                ex.getMessage(),
                Instant.now().toString()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionMessage);

    }
}
