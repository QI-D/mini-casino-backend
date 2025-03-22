package com.casino.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double chanceOfWinning;
    private double winningMultiplier;
    private double maxBet;
    private double minBet;
}
