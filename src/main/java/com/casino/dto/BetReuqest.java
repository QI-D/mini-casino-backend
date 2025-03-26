package com.casino.dto;

import lombok.Data;

@Data
public class BetReuqest {
    private Long gameId;
    private double betAmount;
}
