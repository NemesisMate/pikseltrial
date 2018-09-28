package com.nemesismate.piksel.trial.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<?> handleStatusExceptions(HttpStatusCodeException exception) {
        log.info("Bad client request. Status code: {}. Message: {}.", exception.getStatusCode(), exception.getStatusText());
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(exception.getStatusText());
    }
}
