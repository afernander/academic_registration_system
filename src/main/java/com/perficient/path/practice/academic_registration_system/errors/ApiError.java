package com.perficient.path.practice.academic_registration_system.errors;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ApiError {
    private HttpStatus status;
    private String message;
    private List<String> errors;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    ApiError(HttpStatus status, String message, List<String> errors) {
        this();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    ApiError(HttpStatus status, String message, String error) {
        this();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }


}