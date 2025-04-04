package com.casino.controller;

import com.casino.dto.LoginRequest;
import com.casino.dto.PlayerDto;
import com.casino.dto.Response;
import com.casino.entity.Player;
import com.casino.mapper.PlayerMapper;
import com.casino.security.JwtUtils;
import com.casino.service.PlayerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PlayerService playerService;
    private final PlayerMapper playerMapper;
    private final JwtUtils jwtUtils;

    public AuthController(PlayerService playerService, PlayerMapper playerMapper, JwtUtils jwtUtils) {
        this.playerService = playerService;
        this.playerMapper = playerMapper;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public Response registerPlayer(@RequestBody PlayerDto playerDto) {
        Player registeredPlayer = playerService.registerPlayer(playerMapper.toEntity(playerDto));

        String token = jwtUtils.generateToken(registeredPlayer);
        PlayerDto registeredPlayerDto = playerMapper.toDto(registeredPlayer);

        return Response.builder()
                .status(200)
                .message("Player registered successfully. $100 credit has been deposited.")
                .token(token)
                .player(registeredPlayerDto)
                .build();
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest loginRequest) {
        Player player = playerService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        String token = jwtUtils.generateToken(player);
        PlayerDto playerDto = playerMapper.toDto(player);

        return Response.builder()
                .status(200)
                .message("Login successful")
                .token(token)
                .expirationTime("6 month")
                .player(playerDto)
                .build();
    }
}