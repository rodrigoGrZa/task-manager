package com.imatia.taskmanagerAC.exception;

import java.time.LocalDateTime;

/**
 * Represents an error response for API exceptions, encapsulating the error details.
 */
public class ApiError {
    private final LocalDateTime timestamp;
    private final String message;
    private final int status;

    /**
     * Constructs a new {@code ApiError} with the specified details.
     *
     * @param timestamp the date and time when the error occurred.
     * @param message   a descriptive message of the error.
     * @param status    the HTTP status code corresponding to the error.
     */
    public ApiError(LocalDateTime timestamp, String message, int status) {
        this.timestamp = timestamp;
        this.message = message;
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}