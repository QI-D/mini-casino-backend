package com.casino.mapper;

import com.casino.dto.BetDto;
import com.casino.entity.Bet;
import org.springframework.stereotype.Component;

@Component
public class BetMapper {
    public BetDto toDto(Bet bet) {
        return BetDto.builder()
                .id(bet.getId())
                .playerId(bet.getPlayerId())
                .gameId(bet.getGameId())
                .betAmount(bet.getBetAmount())
                .won(bet.isWon())
                .winnings(bet.getWinnings())
                .build();
    }

    public Bet toEntity(BetDto betDto) {
        Bet bet = new Bet();
        bet.setId(betDto.getId());
        bet.setPlayerId(betDto.getPlayerId());
        bet.setGameId(betDto.getGameId());
        bet.setBetAmount(betDto.getBetAmount());
        bet.setWon(betDto.isWon());
        bet.setWinnings(betDto.getWinnings());
        return bet;
    }
}