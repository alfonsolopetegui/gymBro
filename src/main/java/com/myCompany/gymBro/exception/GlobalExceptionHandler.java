package com.myCompany.gymBro.exception;


import com.myCompany.gymBro.web.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse<Map<String, String>> response = new ApiResponse<>("Validation failed", HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUserNotFoundException(UserNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), HttpStatus.NOT_FOUND.value(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleTokenNotFoundException(TokenNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), HttpStatus.NOT_FOUND.value(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleScheduleNotFoundException(ScheduleNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), HttpStatus.NOT_FOUND.value(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleScheduleNotFoundException(SubscriptionNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), HttpStatus.NOT_FOUND.value(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RegistrationNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleRegistrationNotFoundException(RegistrationNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), HttpStatus.NOT_FOUND.value(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomClassNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleClassNotFoundException(CustomClassNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), HttpStatus.NOT_FOUND.value(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {
        ApiResponse<String> response = new ApiResponse<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
