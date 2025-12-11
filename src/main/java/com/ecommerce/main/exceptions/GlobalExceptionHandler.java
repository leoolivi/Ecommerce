package com.ecommerce.main.exceptions;

import java.time.LocalDateTime;

import javax.naming.OperationNotSupportedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ecommerce.main.data.ErrorResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> OrderNotFoundException(OrderNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> ProductNotFoundException(ProductNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SettingNotFoundException.class)
    public ResponseEntity<?> SettingNotFoundException(SettingNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> EmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO(HttpStatus.CONFLICT, ex.getMessage(), 409, "/api/v1/auth/register", LocalDateTime.now()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> InvalidCredentialsException(InvalidCredentialsException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO(HttpStatus.UNAUTHORIZED, ex.getMessage(), 401, "/api/v1/auth/login", LocalDateTime.now()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(OperationNotSupportedException.class)
    public ResponseEntity<?> OperationNotSupportedException(OperationNotSupportedException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO(HttpStatus.UNAUTHORIZED, ex.getMessage(), 401, "/api/v1/auth", LocalDateTime.now()), HttpStatus.UNAUTHORIZED);
    }
}
