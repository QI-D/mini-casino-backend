package com.casino.mapper;

import com.casino.dto.GameDto;
import com.casino.entity.Game;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {
    public GameDto toDto(Game game) {
        return GameDto.builder()
                .id(game.getId())
                .name(game.getName())
                .chanceOfWinning(game.getChanceOfWinning())
                .winningMultiplier(game.getWinningMultiplier())
                .maxBet(game.getMaxBet())
                .minBet(game.getMinBet())
                .build();
    }

    public Game toEntity(GameDto gameDto) {
        Game game = new Game();
        game.setId(gameDto.getId());
        game.setName(gameDto.getName());
        game.setChanceOfWinning(gameDto.getChanceOfWinning());
        game.setWinningMultiplier(gameDto.getWinningMultiplier());
        game.setMaxBet(gameDto.getMaxBet());
        game.setMinBet(gameDto.getMinBet());
        return game;
    }
}
