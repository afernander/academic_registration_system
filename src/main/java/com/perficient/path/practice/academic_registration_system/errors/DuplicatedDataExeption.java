package com.perficient.path.practice.academic_registration_system.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Duplicated data")
public class DuplicatedDataExeption extends RuntimeException {
    
    public DuplicatedDataExeption(String message) {
        super(message);
    }
}