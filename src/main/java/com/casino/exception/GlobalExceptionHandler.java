package com.casino.exception;

import com.casino.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(Exception e) {
        return ResponseEntity.status(500)
                .body(Response.builder()
                        .status(500)
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(PlayerUnderageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handlePlayerUnderageException(PlayerUnderageException ex) {
        return Response.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handlePlayerNotFoundException(PlayerNotFoundException ex) {
        return Response.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleInsufficientBalanceException(InsufficientBalanceException ex) {
        return Response.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidBetAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleInvalidBetAmountException(InvalidBetAmountException ex) {
        return Response.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(PlayerUsernameExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response handlePlayerUsernameExistsException(PlayerUsernameExistsException ex) {
        return Response.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response handleInvalidCredentialsException(InvalidCredentialsException  ex) {
        return Response.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(DuplicateGameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response handleDuplicateGameException(DuplicateGameException  ex) {
        return Response.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleGameNotFoundException(GameNotFoundException  ex) {
        return Response.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidGameDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleInvalidGameDataException(InvalidGameDataException ex) {
        return Response.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
    }
}