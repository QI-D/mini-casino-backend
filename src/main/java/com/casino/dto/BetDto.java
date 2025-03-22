package com.casino.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BetDto {
    private Long id;
    private Long playerId;
    private Long gameId;
    private double betAmount;
    private boolean won;
    private double winnings;
}