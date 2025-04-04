package com.casino.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private Integer status;
    private String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    private String token;
    private String role;
    private String expirationTime;

    private Integer totalPage;
    private Long totalElement;

    private PlayerDto player;
    private List<PlayerDto> playerList;

    private GameDto game;
    private List<GameDto> gameList;

    private BetDto bet;
    private List<BetDto> betList;

    private Double balance;
    private BetSummaryDto betSummary;
}