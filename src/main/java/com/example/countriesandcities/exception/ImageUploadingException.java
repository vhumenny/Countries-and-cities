package com.example.countriesandcities.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ImageUploadingException extends RuntimeException {
    public static final String IMAGE_ERROR = "Error while uploading image";
    private final HttpStatus httpStatus;

    public ImageUploadingException(final HttpStatus httpStatus, final String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
