package com.example.countriesandcities.config;


import com.example.countriesandcities.dto.response.MessageResponseDto;
import com.example.countriesandcities.exception.ClientException;
import com.example.countriesandcities.exception.ImageUploadingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ClientException.class)
    public MessageResponseDto handleClientException(final ClientException ex) {
        log.warn("{handleClientException} -> {}", ex.getMessage());
        return new MessageResponseDto(ex.getMessage());
    }

    @ExceptionHandler(ImageUploadingException.class)
    public MessageResponseDto handleImageUploadingException(final ImageUploadingException ex) {
        log.warn("{handleImageUploadingException} -> {}", ex.getMessage());
        return new MessageResponseDto(ex.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public MessageResponseDto handleSecurityException(final SecurityException ex) {
        log.warn("{handleSecurityException} -> {}", ex.getMessage());
        return new MessageResponseDto(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public MessageResponseDto handleUnexpectedError(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage());
        return new MessageResponseDto("unexpected error");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ValidationException.class)
    public MessageResponseDto handleValidationException(ValidationException ex) {
        log.error("Validation error: {}", ex.getMessage());
        return new MessageResponseDto(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MessageResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("Incoming request validation error: {}",
                Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage());
        return new MessageResponseDto(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage());
    }
}

