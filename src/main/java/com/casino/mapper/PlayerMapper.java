package com.casino.mapper;

import com.casino.dto.PlayerDto;
import com.casino.entity.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {
    public PlayerDto toDto(Player player) {
        PlayerDto playerDto = new PlayerDto();
        playerDto.setId(player.getId());
        playerDto.setName(player.getName());
        playerDto.setUsername(player.getUsername());
        playerDto.setPassword(player.getPassword());
        playerDto.setBalance(player.getBalance());
        playerDto.setBirthdate(player.getBirthdate());
        return playerDto;
    }

    public Player toEntity(PlayerDto dto) {
        Player player = new Player();
        player.setName(dto.getName());
        player.setUsername(dto.getUsername());
        player.setPassword(dto.getPassword());
        player.setBirthdate(dto.getBirthdate());
        return player;
    }
}
