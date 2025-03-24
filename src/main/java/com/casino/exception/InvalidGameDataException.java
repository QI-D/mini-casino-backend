package com.casino.exception;

public class InvalidGameDataException extends RuntimeException {
    public InvalidGameDataException(String message) {
        super(message);
    }
}