package com.casino.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameDto {
    private Long id;
    private String name;
    private double chanceOfWinning;
    private double winningMultiplier;
    private double maxBet;
    private double minBet;
}