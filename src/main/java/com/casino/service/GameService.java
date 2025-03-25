package com.casino.service;

import com.casino.entity.Game;
import com.casino.exception.GameNotFoundException;
import com.casino.repository.GameRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final GameRepo gameRepo;

    public GameService(GameRepo gameRepo) {
        this.gameRepo = gameRepo;
    }

    public List<Game> getAllGames() {
        return gameRepo.findAll();
    }

    public Game getGameById(Long id) {
        return gameRepo.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));
    }

    public List<Game> saveAllGames(List<Game> games) {
        // Check for duplicates before saving
        List<String> existingNames = gameRepo.findAll().stream()
                .map(Game::getName)
                .collect(Collectors.toList());

        List<Game> uniqueGames = games.stream()
                .filter(game -> !existingNames.contains(game.getName()))
                .collect(Collectors.toList());

        return gameRepo.saveAll(uniqueGames);
    }

    public List<Game> searchGamesByName(String name) {
        return gameRepo.findByNameContainingIgnoreCase(name);
    }
}