package com.casino.controller;

import com.casino.dto.GameDto;
import com.casino.dto.Response;
import com.casino.entity.Game;
import com.casino.exception.DuplicateGameException;
import com.casino.mapper.GameMapper;
import com.casino.repository.GameRepo;
import com.casino.service.GameService;
import com.casino.util.GameXmlParser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;
    private final GameMapper gameMapper;
    private final GameRepo gameRepo;

    public GameController(GameService gameService, GameMapper gameMapper, GameRepo gameRepo) {
        this.gameService = gameService;
        this.gameMapper = gameMapper;
        this.gameRepo = gameRepo;
    }

    @GetMapping
    public Response getAllGames() {
        List<GameDto> gameDtoList = gameService.getAllGames().stream()
                .map(gameMapper::toDto)
                .collect(Collectors.toList());
        return Response.builder()
                .status(200)
                .message("Games retrieved successfully")
                .gameList(gameDtoList)
                .build();
    }

    @GetMapping("/{id}")
    public Response getGameById(@PathVariable Long id) {
        GameDto gameDto = gameMapper.toDto(gameService.getGameById(id));
        return Response.builder()
                .status(200)
                .message("Game retrieved successfully")
                .game(gameDto)
                .build();
    }

    @PostMapping("/upload")
    public Response uploadGames(@RequestParam("file") MultipartFile file) {
        try {
            List<Game> games = GameXmlParser.parseXmlFile(file);
            List<Game> savedGames = gameService.saveAllGames(games);

            return Response.builder()
                    .status(200)
                    .message(savedGames.size() + "/" + games.size() + " games uploaded successfully")
                    .gameList(gameMapper.toDtoList(savedGames))
                    .build();

        } catch (DuplicateGameException e) {
            return Response.builder()
                    .status(400)
                    .message(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .status(500)
                    .message("Failed to upload games: " + e.getMessage())
                    .build();
        }
    }

    @GetMapping("/search")
    public Response searchGames(@RequestParam(required = false) String name) {
        List<GameDto> gameDtoList;

        if (name == null || name.trim().isEmpty()) {
            gameDtoList = gameService.getAllGames().stream()
                    .map(gameMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            gameDtoList = gameService.searchGamesByName(name).stream()
                    .map(gameMapper::toDto)
                    .collect(Collectors.toList());
        }

        return Response.builder()
                .status(200)
                .message("Games found: " + gameDtoList.size())
                .gameList(gameDtoList)
                .build();
    }
}