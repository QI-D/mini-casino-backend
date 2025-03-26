package com.casino.controller;

import com.casino.dto.BetDto;
import com.casino.dto.BetReuqest;
import com.casino.dto.BetSummaryDto;
import com.casino.dto.Response;
import com.casino.entity.Bet;
import com.casino.entity.Player;
import com.casino.mapper.BetMapper;
import com.casino.service.BetService;
import com.casino.service.PlayerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bet")
public class BetController {
    private final BetService betService;
    private final PlayerService playerService;
    private final BetMapper betMapper;

    public BetController(BetService betService, PlayerService playerService, BetMapper betMapper) {
        this.betService = betService;
        this.playerService = playerService;
        this.betMapper = betMapper;
    }

    @GetMapping("/summary")
    public Response getBetSummary(@AuthenticationPrincipal UserDetails userDetails) {
        Player player = playerService.getPlayerByUsername(userDetails.getUsername());
        BetSummaryDto summary = betService.getBetSummary(player.getId());

        return Response.builder()
                .status(200)
                .message("Bet summary retrieved successfully")
                .betSummary(summary)
                .build();
    }

    @PostMapping("/place")
    public Response placeBet(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BetReuqest betRequest) {

        Player player = playerService.getPlayerByUsername(userDetails.getUsername());
        Bet placedBet = betService.placeBet(player.getId(), betRequest.getGameId(), betRequest.getBetAmount());
        BetDto betDto = betMapper.toDto(placedBet);

        return Response.builder()
                .status(200)
                .message("Bet placed successfully")
                .bet(betDto)
                .build();
    }
}