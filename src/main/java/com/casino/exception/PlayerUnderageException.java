package com.casino.exception;

public class PlayerUnderageException extends RuntimeException {
    public PlayerUnderageException(String message) {
        super(message);
    }
}