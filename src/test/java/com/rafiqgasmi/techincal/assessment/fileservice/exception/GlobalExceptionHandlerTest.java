package com.rafiqgasmi.techincal.assessment.fileservice.exception;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();

    @Test
    public void handleExceptionTest() {
        Exception exception = new Exception("Test Exception");
        ResponseEntity<String> result = handler.handleException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Test Exception", result.getBody());
    }

    @Test
    public void handleIllegalArgumentExceptionTest() {
        IllegalArgumentException exception = new IllegalArgumentException("Test IllegalArgumentException");
        ResponseEntity<String> result = handler.handleIllegalArgumentException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Test IllegalArgumentException", result.getBody());
    }
}