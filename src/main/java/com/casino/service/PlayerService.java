package com.casino.service;

import com.casino.entity.Player;
import com.casino.exception.PlayerNotFoundException;
import com.casino.exception.PlayerUnderageException;
import com.casino.exception.PlayerUsernameExistsException;
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
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PlayerService(PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    public Player registerPlayer(Player player) {
        // Validate player age
        if (isUnderage(player.getBirthdate())) {
            throw new PlayerUnderageException("Player must be at least 18 years old");
        }

        // Check if username is unique
        if (usernameExists(player.getUsername())) {
            throw new PlayerUsernameExistsException("Username already exists");
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(player.getPassword());
        player.setPassword(hashedPassword);

        // Save the player
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

    public Player getPlayerById(Long id) {
        return playerRepo.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));
    }

    public void deposit(Long playerId, double amount) {
        Player player = getPlayerById(playerId);
        player.setBalance(player.getBalance() + amount);
        playerRepo.save(player);
    }

    public double getBalance(Long playerId) {
        Player player = getPlayerById(playerId);
        return player.getBalance();
    }
}