package com.matalban.pollsystem.controllers;

import com.matalban.pollsystem.api.v0.dto.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.stream.Collectors;

@ControllerAdvice
public class ResponseStatusExceptionHandler {

    //Handles the Response Status Exceptions throws in the services
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
    }


    //Handles @Valid validation errors in @RequestBody DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        // Collect all validation messages into one string
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                new Date(),
                String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()), // 422
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    //Handles JSON deserialization errors (e.g., invalid date format)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidationErrorResponse> handleInvalidFormat(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        String message = "Invalid request format";
        message += ": " + ex.getMostSpecificCause().getMessage();

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                new Date(),
                String.valueOf(HttpStatus.BAD_REQUEST.value()), // 400
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
