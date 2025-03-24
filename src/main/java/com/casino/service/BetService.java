package com.casino.service;

import com.casino.dto.BetSummaryDto;
import com.casino.entity.Bet;
import com.casino.entity.Game;
import com.casino.entity.Player;
import com.casino.exception.InsufficientBalanceException;
import com.casino.exception.InvalidBetAmountException;
import com.casino.repository.BetRepo;
import com.casino.repository.GameRepo;
import com.casino.repository.PlayerRepo;
import com.casino.specification.BetSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class BetService {
    private final BetRepo betRepo;
    private final PlayerRepo playerRepo;
    private final GameRepo gameRepo;

    public BetService(BetRepo betRepo, PlayerRepo playerRepo, GameRepo gameRepo) {
        this.betRepo = betRepo;
        this.playerRepo = playerRepo;
        this.gameRepo = gameRepo;
    }

    public Bet placeBet(Long playerId, Long gameId, double betAmount) {
        Player player = playerRepo.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        Game game = gameRepo.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        // Validate bet amount
        if (betAmount < game.getMinBet() || betAmount > game.getMaxBet()) {
            throw new InvalidBetAmountException("Bet amount is outside the allowed range");
        }

        // Check if the player has enough balance
        if (betAmount > player.getBalance()) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // Simulate the bet outcome
        boolean won = new Random().nextDouble() < game.getChanceOfWinning();
        double winnings = won ? betAmount * game.getWinningMultiplier() : 0;

        // Update player balance
        player.setBalance(player.getBalance() - betAmount + winnings);
        playerRepo.save(player);

        // Create and save the bet
        Bet bet = new Bet();
        bet.setPlayerId(playerId);
        bet.setGameId(gameId);
        bet.setBetAmount(betAmount);
        bet.setWon(won);
        bet.setWinnings(winnings);

        return betRepo.save(bet);
    }

    public BetSummaryDto getBetSummary(Long playerId) {
        List<Bet> playerBets = betRepo.findByPlayerId(playerId);

        int totalBets = playerBets.size();
        double totalBetAmount = playerBets.stream()
                .mapToDouble(Bet::getBetAmount)
                .sum();
        double totalWinnings = playerBets.stream()
                .mapToDouble(Bet::getWinnings)
                .sum();

        return BetSummaryDto.builder()
                .totalBets(totalBets)
                .totalBetAmount(totalBetAmount)
                .totalWinnings(totalWinnings)
                .netProfit(totalWinnings - totalBetAmount)
                .build();
    }
}