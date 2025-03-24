package com.casino.controller;

import com.casino.dto.Response;
import com.casino.exception.PlayerNotFoundException;
import com.casino.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    private UserDetails userDetails;
    private static final String TEST_USERNAME = "testuser";
    private static final double TEST_BALANCE = 1000.0;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetails = new User(TEST_USERNAME, "password", Collections.emptyList());
    }

    @Test
    void deposit_ShouldReturnSuccessResponse_WhenValidAmount() {
        double depositAmount = 500.0;
        double expectedBalance = TEST_BALANCE + depositAmount;
        when(playerService.deposit(TEST_USERNAME, depositAmount)).thenReturn(expectedBalance);

        Response response = playerController.deposit(userDetails, depositAmount);

        assertEquals(200, response.getStatus());
        assertEquals("Deposit successful", response.getMessage());
        assertEquals(expectedBalance, response.getBalance());
        verify(playerService, times(1)).deposit(TEST_USERNAME, depositAmount);
    }

    @Test
    void deposit_ShouldThrowIllegalArgumentException_WhenNegativeAmount() {
        double invalidAmount = -100.0;
        when(playerService.deposit(TEST_USERNAME, invalidAmount))
                .thenThrow(new IllegalArgumentException("Deposit amount must be positive"));

        assertThrows(IllegalArgumentException.class, () ->
                playerController.deposit(userDetails, invalidAmount));
    }

    @Test
    void deposit_ShouldThrowPlayerNotFoundException_WhenPlayerNotFound() {
        double amount = 200.0;
        when(playerService.deposit(TEST_USERNAME, amount))
                .thenThrow(new PlayerNotFoundException("Player not found"));

        assertThrows(PlayerNotFoundException.class, () ->
                playerController.deposit(userDetails, amount));
    }

    @Test
    void getBalance_ShouldReturnCurrentBalance_WhenPlayerExists() {
        when(playerService.getBalance(TEST_USERNAME)).thenReturn(TEST_BALANCE);

        Response response = playerController.getBalance(userDetails);

        assertEquals(200, response.getStatus());
        assertEquals("Balance retrieved successfully", response.getMessage());
        assertEquals(TEST_BALANCE, response.getBalance());
        verify(playerService, times(1)).getBalance(TEST_USERNAME);
    }

    @Test
    void getBalance_ShouldThrowPlayerNotFoundException_WhenPlayerNotFound() {
        when(playerService.getBalance(TEST_USERNAME))
                .thenThrow(new PlayerNotFoundException("Player not found"));

        assertThrows(PlayerNotFoundException.class, () ->
                playerController.getBalance(userDetails));
    }

    @Test
    void deposit_ShouldHandleZeroAmount() {
        double zeroAmount = 0.0;
        when(playerService.deposit(TEST_USERNAME, zeroAmount))
                .thenThrow(new IllegalArgumentException("Deposit amount must be positive"));

        assertThrows(IllegalArgumentException.class, () ->
                playerController.deposit(userDetails, zeroAmount));
    }

    @Test
    void deposit_ShouldHandleVeryLargeAmount() {
        double largeAmount = 1_000_000.0;
        double expectedBalance = TEST_BALANCE + largeAmount;
        when(playerService.deposit(TEST_USERNAME, largeAmount)).thenReturn(expectedBalance);

        Response response = playerController.deposit(userDetails, largeAmount);

        assertEquals(200, response.getStatus());
        assertEquals(expectedBalance, response.getBalance());
    }

    @Test
    void getBalance_ShouldReturnZeroForNewPlayer() {
        when(playerService.getBalance(TEST_USERNAME)).thenReturn(0.0);

        Response response = playerController.getBalance(userDetails);

        assertEquals(200, response.getStatus());
        assertEquals(0.0, response.getBalance());
    }
}