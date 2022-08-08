package com.perficient.path.practice.academic_registration_system.errors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ApiErrorTest {

    @Test
    void apiConstructorTest1() {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
        assertNull(apiError.getMessage());
        assertNull(apiError.getErrors());
        assertNotNull(apiError.getTimestamp());
    }

    @Test
    void apiConstructorTest2() {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "message", Arrays.asList("error1", "error2"));
        assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
        assertEquals("message", apiError.getMessage());
        assertEquals(Arrays.asList("error1", "error2"), apiError.getErrors());
        assertNotNull(apiError.getTimestamp());
    }

    @Test
    void apiConstructorTest3() {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "message", "error");
        assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
        assertEquals("message", apiError.getMessage());
        assertEquals("error", apiError.getErrors().get(0));
        assertNotNull(apiError.getTimestamp());
    }

    
}
