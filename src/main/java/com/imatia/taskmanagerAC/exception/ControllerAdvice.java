package com.imatia.taskmanagerAC.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * Global exception handler to manage and respond to exceptions in a standardized way.
 */
@ControllerAdvice
class GlobalExceptionHandler {

    /**
     * Handles {@link ResourceNotFoundException} and returns a response with HTTP 404 status.
     *
     * @param ex the exception thrown when a resource is not found.
     * @return a {@link ResponseEntity} containing the {@link ApiError} and the HTTP status.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiError error = new ApiError(LocalDateTime.now(), ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles {@link IllegalArgumentException} and returns a response with HTTP 400 status.
     *
     * @param ex the exception thrown due to invalid arguments.
     * @return a {@link ResponseEntity} containing the {@link ApiError} and the HTTP status.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiError error = new ApiError(LocalDateTime.now(), ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles general {@link Exception} and returns a response with HTTP 500 status.
     *
     * @param ex the exception thrown due to an unexpected error.
     * @return a {@link ResponseEntity} containing the {@link ApiError} and the HTTP status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception ex) {
        ApiError error = new ApiError(LocalDateTime.now(), "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
