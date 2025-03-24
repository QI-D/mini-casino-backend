package com.casino.service;

import com.casino.entity.Game;
import com.casino.repository.GameRepo;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(() -> new RuntimeException("Game not found"));
    }

    public void saveAllGames(List<Game> games) {
        gameRepo.saveAll(games);
    }
}