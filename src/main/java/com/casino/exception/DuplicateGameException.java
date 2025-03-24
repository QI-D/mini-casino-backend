package com.casino.exception;

public class DuplicateGameException extends RuntimeException {
    public DuplicateGameException(String message) {
        super(message);
    }
}