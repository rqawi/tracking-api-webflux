package com.example.tracking.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import reactor.test.StepVerifier;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    public void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleException_returnsInternalServerError() {
        Exception ex = new Exception("Test general exception");

        StepVerifier.create(exceptionHandler.handleException(ex))
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR &&
                                response.getBody().equals("An unexpected error occurred. Please contact support.")
                )
                .verifyComplete();
    }

    @Test
    public void testHandleIllegalState_returnsConflictStatus() {
        IllegalStateException ex = new IllegalStateException("Test illegal state");

        StepVerifier.create(exceptionHandler.handleIllegalState(ex))
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.CONFLICT &&
                                response.getBody().equals("Test illegal state")
                )
                .verifyComplete();
    }
}
