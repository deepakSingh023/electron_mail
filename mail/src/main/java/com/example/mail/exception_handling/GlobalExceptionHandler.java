package com.example.mail.exception_handling;


import com.example.mail.dto.ExceptionMessage;
import com.example.mail.exception_handling.exception.UserAlreadyExist;
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
                HttpStatus.ALREADY_REPORTED.toString(),
                "Already exists",
                ex.getMessage(),
                Instant.now().toString()
        );

        return new ResponseEntity<>(exceptionMessage,HttpStatus.ALREADY_REPORTED);

    }
}
