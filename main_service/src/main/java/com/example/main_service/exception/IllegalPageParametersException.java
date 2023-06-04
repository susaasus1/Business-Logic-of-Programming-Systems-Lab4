package com.example.main_service.exception;

public class IllegalPageParametersException extends RuntimeException {
    public IllegalPageParametersException(String message) {
        super(message);
    }
}
