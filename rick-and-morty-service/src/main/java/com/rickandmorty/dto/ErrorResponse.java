package com.rickandmorty.dto;

import java.time.LocalDateTime;

public final class ErrorResponse {
    private final String error;
    private final String message;
    private final LocalDateTime timestamp;

    ErrorResponse(String error, String message, LocalDateTime timestamp) {
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }
    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ErrorResponse(String error, String message) {
        this(error, message, LocalDateTime.now());
    }
}
