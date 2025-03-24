package com.casino.controller;

import com.casino.dto.LoginRequest;
import com.casino.dto.PlayerDto;
import com.casino.dto.Response;
import com.casino.entity.Player;
import com.casino.mapper.PlayerMapper;
import com.casino.security.JwtUtils;
import com.casino.service.PlayerService;
import org.springframework.transaction.annotation.Transactional;
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
        PlayerDto registeredPlayer = playerMapper.toDto(playerService.registerPlayer(playerMapper.toEntity(playerDto)));
        return Response.builder()
                .status(200)
                .message("Player registered successfully")
                .player(registeredPlayer)
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

    @DeleteMapping("/delete/{username}")
    @Transactional
    public Response deletePlayer(@PathVariable String username) {
        playerService.deletePlayer(username);
        return Response.builder()
                .status(200)
                .message("Player deleted successfully")
                .build();
    }
}