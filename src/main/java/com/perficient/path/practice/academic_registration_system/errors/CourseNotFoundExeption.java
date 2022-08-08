package com.perficient.path.practice.academic_registration_system.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CourseNotFoundExeption extends RuntimeException{
    
    public CourseNotFoundExeption(String message) {
        super(message);
    }
    
}