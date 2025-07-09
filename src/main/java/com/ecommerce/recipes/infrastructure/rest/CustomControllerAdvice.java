package com.ecommerce.recipes.infrastructure.rest;

import com.ecommerce.recipes.infrastructure.models.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(ResponseStatusException.class) // exception handled
    public ResponseEntity<ErrorResponse> handleExceptions(
            ResponseStatusException e
    ) {
        var status = HttpStatus.valueOf(e.getStatusCode().value());

        return new ResponseEntity<>(
                new ErrorResponse(
                        status,
                        "An unexpected error occurred. Please try again later."
                ),
                status
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolatedExceptions(
            Exception e
    ) {

        var status = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(
                new ErrorResponse(
                        status,
                        e.getMessage()
                ),
                status
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundExceptions(
            Exception e
    ) {

        var status = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(
                new ErrorResponse(
                        status,
                        e.getMessage()
                ),
                status
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> fallBackHandleExceptions(
            Exception e
    ) {

        var status = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(
                new ErrorResponse(
                        status,
                        "An unexpected error occurred. Please try again later."
                ),
                status
        );
    }
}
