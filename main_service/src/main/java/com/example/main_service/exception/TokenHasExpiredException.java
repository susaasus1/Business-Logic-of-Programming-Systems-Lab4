package com.example.main_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenHasExpiredException extends RuntimeException {
    public TokenHasExpiredException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
