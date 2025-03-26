package com.casino.controller;

import com.casino.dto.DepositRequest;
import com.casino.dto.Response;
import com.casino.service.PlayerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }


    @PostMapping("/deposit")
    public Response deposit(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DepositRequest depositRequest) {
        String username = userDetails.getUsername();
        double newBalance = playerService.deposit(username, depositRequest.getAmount());

        return Response.builder()
                .status(200)
                .message("Deposit successful")
                .balance(newBalance)
                .build();
    }

    @GetMapping("/balance")
    public Response getBalance(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        double balance = playerService.getBalance(username);

        return Response.builder()
                .status(200)
                .message("Balance retrieved successfully")
                .balance(balance)
                .build();
    }
}