package com.casino.controller;

import com.casino.dto.LoginRequest;
import com.casino.dto.PlayerDto;
import com.casino.dto.Response;
import com.casino.entity.Player;
import com.casino.exception.PlayerUnderageException;
import com.casino.mapper.PlayerMapper;
import com.casino.security.JwtUtils;
import com.casino.service.PlayerService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private PlayerMapper playerMapper;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "testpass123";
    private Player testPlayer;
    private PlayerDto testPlayerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test player
        testPlayer = new Player();
        testPlayer.setUsername(TEST_USERNAME);
        testPlayer.setPassword("encodedPassword");
        testPlayer.setBirthdate(LocalDate.of(1990, 1, 1));

        testPlayerDto = new PlayerDto();
        testPlayerDto.setUsername(TEST_USERNAME);
        testPlayerDto.setPassword(TEST_PASSWORD);
    }

    @Test
    @DisplayName("Should reject registration for underage player")
    void testRegister_UnderagePlayer() {
        // Arrange
        PlayerDto underagePlayer = new PlayerDto();
        underagePlayer.setUsername("underage_user");
        underagePlayer.setPassword("test123");
        underagePlayer.setBirthdate(LocalDate.now().minusYears(17)); // 17 years old

        when(playerMapper.toEntity(underagePlayer)).thenReturn(new Player());
        when(playerService.registerPlayer(any()))
                .thenThrow(new PlayerUnderageException("Player must be at least 18 years old"));

        // Act & Assert
        Exception exception = assertThrows(PlayerUnderageException.class, () -> {
            authController.registerPlayer(underagePlayer);
        });

        assertEquals("Player must be at least 18 years old", exception.getMessage());
        verify(playerService, times(1)).registerPlayer(any());
    }

    @Test
    @DisplayName("Should register new player successfully")
    void testRegisterPlayer_Success() {
        // Arrange
        when(playerMapper.toEntity(any(PlayerDto.class))).thenReturn(testPlayer);
        when(playerService.registerPlayer(any(Player.class))).thenReturn(testPlayer);
        when(playerMapper.toDto(any(Player.class))).thenReturn(testPlayerDto);

        // Act
        Response response = authController.registerPlayer(testPlayerDto);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals("Player registered successfully", response.getMessage());
        assertEquals(TEST_USERNAME, response.getPlayer().getUsername());
        verify(playerService, times(1)).registerPlayer(any(Player.class));
    }

    @Test
    @DisplayName("Should login successfully with valid credentials")
    void testLogin_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(TEST_USERNAME);
        loginRequest.setPassword(TEST_PASSWORD);
        when(playerService.authenticate(TEST_USERNAME, TEST_PASSWORD)).thenReturn(testPlayer);
        when(jwtUtils.generateToken(testPlayer)).thenReturn("mockToken");
        when(playerMapper.toDto(testPlayer)).thenReturn(testPlayerDto);

        // Act
        Response response = authController.login(loginRequest);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals("Login successful", response.getMessage());
        assertEquals("mockToken", response.getToken());
        assertEquals(TEST_USERNAME, response.getPlayer().getUsername());
    }

    @Test
    @DisplayName("Should fail login with invalid credentials")
    void testLogin_InvalidCredentials() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(TEST_USERNAME);
        loginRequest.setPassword("wrongpassword");
        when(playerService.authenticate(TEST_USERNAME, "wrongpassword"))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authController.login(loginRequest));
    }

//    @Test
//    @DisplayName("Should delete player successfully")
//    void testDeletePlayer_Success() {
//        Response response = authController.deletePlayer(TEST_USERNAME);
//
//        assertEquals(200, response.getStatus());
//        assertEquals("Player deleted successfully", response.getMessage());
//        verify(playerService, times(1)).deletePlayer(TEST_USERNAME);
//    }
}