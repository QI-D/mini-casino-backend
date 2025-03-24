package com.casino.controller;

import com.casino.dto.GameDto;
import com.casino.dto.Response;
import com.casino.entity.Game;
import com.casino.exception.DuplicateGameException;
import com.casino.exception.GameNotFoundException;
import com.casino.mapper.GameMapper;
import com.casino.repository.GameRepo;
import com.casino.service.GameService;
import com.casino.util.GameXmlParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GameControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private GameMapper gameMapper;

    @Mock
    private GameRepo gameRepo;

    @InjectMocks
    private GameController gameController;

    private Game testGame1;
    private Game testGame2;
    private GameDto testGameDto1;
    private GameDto testGameDto2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testGame1 = new Game();
        testGame1.setId(1L);
        testGame1.setName("Blackjack");
        testGame1.setChanceOfWinning(0.42);
        testGame1.setWinningMultiplier(2.0);
        testGame1.setMinBet(10.0);
        testGame1.setMaxBet(1000.0);

        testGame2 = new Game();
        testGame2.setId(2L);
        testGame2.setName("Roulette");
        testGame2.setChanceOfWinning(0.48);
        testGame2.setWinningMultiplier(1.8);
        testGame2.setMinBet(5.0);
        testGame2.setMaxBet(500.0);

        testGameDto1 = GameDto.builder()
                .id(1L)
                .name("Blackjack")
                .chanceOfWinning(0.42)
                .winningMultiplier(2.0)
                .minBet(10.0)
                .maxBet(1000.0)
                .build();

        testGameDto2 = GameDto.builder()
                .id(2L)
                .name("Roulette")
                .chanceOfWinning(0.48)
                .winningMultiplier(1.8)
                .minBet(5.0)
                .maxBet(500.0)
                .build();
    }

    @Test
    void getAllGames_ShouldReturnAllGames() {
        // Arrange
        List<Game> games = Arrays.asList(testGame1, testGame2);
        List<GameDto> gameDtos = Arrays.asList(testGameDto1, testGameDto2);

        when(gameService.getAllGames()).thenReturn(games);
        when(gameMapper.toDto(testGame1)).thenReturn(testGameDto1);
        when(gameMapper.toDto(testGame2)).thenReturn(testGameDto2);

        // Act
        Response response = gameController.getAllGames();

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals("Games retrieved successfully", response.getMessage());
        assertEquals(2, response.getGameList().size());
        assertTrue(response.getGameList().containsAll(gameDtos));
        verify(gameService, times(1)).getAllGames();
    }

    @Test
    void getGameById_ShouldReturnGame_WhenExists() {
        when(gameService.getGameById(1L)).thenReturn(testGame1);
        when(gameMapper.toDto(testGame1)).thenReturn(testGameDto1);

        Response response = gameController.getGameById(1L);

        assertEquals(200, response.getStatus());
        assertEquals("Game retrieved successfully", response.getMessage());
        assertEquals(testGameDto1, response.getGame());
        verify(gameService, times(1)).getGameById(1L);
    }

    @Test
    void getGameById_ShouldThrowException_WhenNotFound() {
        when(gameService.getGameById(99L))
                .thenThrow(new GameNotFoundException("Game not found"));

        assertThrows(GameNotFoundException.class, () -> gameController.getGameById(99L));
    }

    @Test
    void uploadGames_ShouldReturnSuccess_WhenValidFile() throws Exception {
        // Arrange
        String xmlContent = """
            <games>
                <game>
                    <name>Blackjack</name>
                    <chanceOfWinning>0.42</chanceOfWinning>
                    <winningMultiplier>2.0</winningMultiplier>
                    <minBet>10.0</minBet>
                    <maxBet>1000.0</maxBet>
                </game>
            </games>
            """;
        MultipartFile file = new MockMultipartFile(
                "games.xml", "games.xml", "text/xml", xmlContent.getBytes());

        List<Game> parsedGames = List.of(testGame1);
        List<Game> savedGames = List.of(testGame1);
        List<GameDto> savedGameDtos = List.of(testGameDto1);

        // Mock the static parseXmlFile method
        try (MockedStatic<GameXmlParser> mockedParser = mockStatic(GameXmlParser.class)) {
            mockedParser.when(() -> GameXmlParser.parseXmlFile(file))
                    .thenReturn(parsedGames);

            when(gameService.saveAllGames(parsedGames)).thenReturn(savedGames);
            when(gameMapper.toDtoList(savedGames)).thenReturn(savedGameDtos);

            // Act
            Response response = gameController.uploadGames(file);

            // Assert
            assertEquals(200, response.getStatus());
            assertEquals("1/1 games uploaded successfully", response.getMessage());
            assertEquals(1, response.getGameList().size());
            verify(gameService, times(1)).saveAllGames(parsedGames);
        }
    }

    @Test
    void uploadGames_ShouldReturnError_WhenDuplicateGames() throws Exception {
        String xmlContent = """
                <games>
                    <game>
                        <name>Blackjack</name>
                        <chanceOfWinning>0.42</chanceOfWinning>
                        <winningMultiplier>2.0</winningMultiplier>
                        <minBet>10.0</minBet>
                        <maxBet>1000.0</maxBet>
                    </game>
                </games>
                """;
        MultipartFile file = new MockMultipartFile(
                "games.xml", "games.xml", "text/xml", xmlContent.getBytes());

        when(gameService.saveAllGames(any()))
                .thenThrow(new DuplicateGameException("1 duplicate games found"));

        Response response = gameController.uploadGames(file);

        assertEquals(400, response.getStatus());
        assertEquals("1 duplicate games found", response.getMessage());
    }

    @Test
    void uploadGames_ShouldReturnError_WhenInvalidFile() throws Exception {
        MultipartFile file = new MockMultipartFile(
                "invalid.xml", "invalid.xml", "text/xml", "invalid content".getBytes());

        Response response = gameController.uploadGames(file);

        assertEquals(500, response.getStatus());
        assertTrue(response.getMessage().startsWith("Failed to upload games:"));
    }

    @Test
    void uploadGames_ShouldReturnError_WhenEmptyFile() throws Exception {
        MultipartFile file = new MockMultipartFile(
                "empty.xml", "empty.xml", "text/xml", new byte[0]);

        Response response = gameController.uploadGames(file);

        assertEquals(500, response.getStatus());
        assertTrue(response.getMessage().startsWith("Failed to upload games:"));
    }
}