package com.casino.controller;

import com.casino.dto.BetDto;
import com.casino.dto.BetReuqest;
import com.casino.dto.BetSummaryDto;
import com.casino.dto.Response;
import com.casino.entity.Bet;
import com.casino.entity.Player;
import com.casino.exception.*;
import com.casino.mapper.BetMapper;
import com.casino.service.BetService;
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

class BetControllerTest {

    @Mock
    private BetService betService;

    @Mock
    private PlayerService playerService;

    @Mock
    private BetMapper betMapper;

    @InjectMocks
    private BetController betController;

    private UserDetails userDetails;
    private Player testPlayer;
    private Bet testBet;
    private BetDto testBetDto;
    private BetSummaryDto testSummary;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDetails = new User("testuser", "password", Collections.emptyList());

        testPlayer = new Player();
        testPlayer.setId(1L);
        testPlayer.setUsername("testuser");
        testPlayer.setBalance(1000.0);

        testBet = new Bet();
        testBet.setId(1L);
        testBet.setPlayerId(1L);
        testBet.setGameId(1L);
        testBet.setBetAmount(100.0);
        testBet.setWon(true);
        testBet.setWinnings(200.0);

        testBetDto = BetDto.builder()
                .id(1L)
                .playerId(1L)
                .gameId(1L)
                .betAmount(100.0)
                .won(true)
                .winnings(200.0)
                .build();

        testSummary = BetSummaryDto.builder()
                .totalBets(5)
                .totalBetAmount(500.0)
                .totalWinnings(600.0)
                .netProfit(100.0)
                .build();
    }

    @Test
    void getBetSummary_ShouldReturnSummary_WhenPlayerExists() {
        when(playerService.getPlayerByUsername("testuser")).thenReturn(testPlayer);
        when(betService.getBetSummary(1L)).thenReturn(testSummary);

        Response response = betController.getBetSummary(userDetails);

        assertEquals(200, response.getStatus());
        assertEquals("Bet summary retrieved successfully", response.getMessage());
        assertEquals(testSummary, response.getBetSummary());
        verify(playerService, times(1)).getPlayerByUsername("testuser");
        verify(betService, times(1)).getBetSummary(1L);
    }

    @Test
    void getBetSummary_ShouldThrowException_WhenPlayerNotFound() {
        when(playerService.getPlayerByUsername("testuser"))
                .thenThrow(new PlayerNotFoundException("Player not found"));

        assertThrows(PlayerNotFoundException.class, () ->
                betController.getBetSummary(userDetails));
    }

    @Test
    void placeBet_ShouldReturnSuccess_WhenValidBet() {
        BetReuqest betRequest = new BetReuqest();
        betRequest.setGameId(1L);
        betRequest.setBetAmount(100.0);

        when(playerService.getPlayerByUsername("testuser")).thenReturn(testPlayer);
        when(betService.placeBet(1L, 1L, 100.0)).thenReturn(testBet);
        when(betMapper.toDto(testBet)).thenReturn(testBetDto);

        Response response = betController.placeBet(userDetails, betRequest);

        assertEquals(200, response.getStatus());
        assertEquals("Bet placed successfully", response.getMessage());
        assertEquals(testBetDto, response.getBet());
        verify(betService, times(1)).placeBet(1L, 1L, 100.0);
    }

    @Test
    void placeBet_ShouldThrowException_WhenInvalidBetAmount() {
        BetReuqest betRequest = new BetReuqest();
        betRequest.setGameId(1L);
        betRequest.setBetAmount(5000.0);

        when(playerService.getPlayerByUsername("testuser")).thenReturn(testPlayer);
        when(betService.placeBet(1L, 1L, 5000.0))
                .thenThrow(new InvalidBetAmountException("Bet amount is outside the allowed range"));

        assertThrows(InvalidBetAmountException.class, () ->
                betController.placeBet(userDetails, betRequest));
    }

    @Test
    void placeBet_ShouldThrowException_WhenInsufficientBalance() {
        BetReuqest betRequest = new BetReuqest();
        betRequest.setGameId(1L);
        betRequest.setBetAmount(2000.0);

        when(playerService.getPlayerByUsername("testuser")).thenReturn(testPlayer);
        when(betService.placeBet(1L, 1L, 2000.0))
                .thenThrow(new InsufficientBalanceException("Insufficient balance"));

        assertThrows(InsufficientBalanceException.class, () ->
                betController.placeBet(userDetails, betRequest));
    }

    @Test
    void placeBet_ShouldThrowException_WhenGameNotFound() {
        BetReuqest betRequest = new BetReuqest();
        betRequest.setGameId(99L);
        betRequest.setBetAmount(100.0);

        when(playerService.getPlayerByUsername("testuser")).thenReturn(testPlayer);
        when(betService.placeBet(1L, 99L, 100.0))
                .thenThrow(new GameNotFoundException("Game not found"));

        assertThrows(GameNotFoundException.class, () ->
                betController.placeBet(userDetails, betRequest));
    }

    @Test
    void placeBet_ShouldThrowException_WhenPlayerNotFound() {

        BetReuqest betRequest = new BetReuqest();
        betRequest.setGameId(1L);
        betRequest.setBetAmount(100.0);

        when(playerService.getPlayerByUsername("testuser"))
                .thenThrow(new PlayerNotFoundException("Player not found"));

        assertThrows(PlayerNotFoundException.class, () ->
                betController.placeBet(userDetails, betRequest));
    }
}