package com.casino.service;

import com.casino.entity.Player;
import com.casino.exception.PlayerNotFoundException;
import com.casino.exception.PlayerUnderageException;
import com.casino.exception.PlayerUsernameExistsException;
import com.casino.repository.BetRepo;
import com.casino.repository.PlayerRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@Slf4j
public class PlayerService {
    private final PlayerRepo playerRepo;
    private final BetRepo betRepo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PlayerService(PlayerRepo playerRepo, BetRepo betRepo) {

        this.playerRepo = playerRepo;
        this.betRepo = betRepo;
    }

    public Player registerPlayer(Player player) {
        if (isUnderage(player.getBirthdate())) {
            throw new PlayerUnderageException("Player must be at least 18 years old");
        }

        if (usernameExists(player.getUsername())) {
            throw new PlayerUsernameExistsException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(player.getPassword());
        player.setPassword(hashedPassword);

        return playerRepo.save(player);
    }

    public boolean usernameExists(String username) {
        return playerRepo.existsByUsername(username);
    }

    private boolean isUnderage(LocalDate birthdate) {
        LocalDate today = LocalDate.now();
        Period age = Period.between(birthdate, today);
        return age.getYears() < 18;
    }

    public Player authenticate(String username, String password) {
        Player player = playerRepo.findByUsername(username)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        if (!passwordEncoder.matches(password, player.getPassword())) {
            throw new PlayerNotFoundException("Invalid password");
        }

        return player;
    }

    public Player getPlayerByUsername(String username) {
        return playerRepo.findByUsername(username)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));
    }

    public double deposit(String username, double amount) {
        Player player = playerRepo.findByUsername(username)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        player.setBalance(player.getBalance() + amount);
        playerRepo.save(player);

        return player.getBalance();
    }

    public double getBalance(String username) {
        Player player = playerRepo.findByUsername(username)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        return player.getBalance();
    }

    public void deletePlayer(String username) {
        Player player = playerRepo.findByUsername(username)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        playerRepo.delete(player);
    }
}