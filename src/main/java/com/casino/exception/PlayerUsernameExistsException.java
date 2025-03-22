package com.casino.exception;

public class PlayerUsernameExistsException extends RuntimeException {
    public PlayerUsernameExistsException(String message) {
        super(message);
    }
}