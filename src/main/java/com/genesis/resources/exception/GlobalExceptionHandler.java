package com.genesis.resources.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex)
    {
        logger.warn("UserNotFoundException: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidPersonIDException.class)
    public ResponseEntity<String> handleInvalidPersonID(InvalidPersonIDException ex)
    {
        logger.warn("InvalidPersonIDException: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(PersonIDAlreadyUsedException.class)
    public ResponseEntity<String> handlePersonIDAlreadyUsed(PersonIDAlreadyUsedException ex)
    {
        logger.warn("PersonIDAlreadyUsedException: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOther(Exception ex)
    {
        logger.error("Unexpected error", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
    }
}
