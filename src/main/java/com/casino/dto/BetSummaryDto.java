package com.casino.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BetSummaryDto {
    private int totalBets;
    private double totalBetAmount;
    private double totalWinnings;
    private double netProfit;
}