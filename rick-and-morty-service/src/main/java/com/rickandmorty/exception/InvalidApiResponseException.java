package com.rickandmorty.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class InvalidApiResponseException extends RuntimeException {

    public InvalidApiResponseException(String message) {
        super(message);
    }

    public InvalidApiResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
