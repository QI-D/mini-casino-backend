package com.casino.util;

import com.casino.entity.Game;
import com.casino.exception.DuplicateGameException;
import com.casino.exception.InvalidGameDataException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameXmlParser {

    public static List<Game> parseXmlFile(MultipartFile file) throws Exception {
        try {
            if (file == null || file.isEmpty()) {
                throw new InvalidGameDataException("XML file is empty or null");
            }

            JAXBContext context = JAXBContext.newInstance(GameListWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            GameListWrapper wrapper = (GameListWrapper) unmarshaller.unmarshal(file.getInputStream());

            List<Game> games = wrapper.getGames();
            validateGames(games);

            return games;

        } catch (IOException e) {
            throw new InvalidGameDataException("Error reading XML file: " + e.getMessage());
        } catch (Exception e) {
            throw new InvalidGameDataException("Invalid XML format: " + e.getMessage());
        }
    }

    private static void validateGames(List<Game> games) {
        if (games == null || games.isEmpty()) {
            throw new InvalidGameDataException("No games found in XML");
        }

        Set<String> gameNames = new HashSet<>();
        for (Game game : games) {
            if (game.getName() == null || game.getName().trim().isEmpty()) {
                throw new InvalidGameDataException("Game name cannot be empty");
            }

            if (!gameNames.add(game.getName())) {
                throw new DuplicateGameException("Duplicate game name in XML: " + game.getName());
            }

            validateGameParameters(game);
        }
    }

    private static void validateGameParameters(Game game) {
        if (game.getChanceOfWinning() <= 0 || game.getChanceOfWinning() > 1) {
            throw new InvalidGameDataException(
                    "Invalid chance of winning for game " + game.getName() +
                            ". Must be between 0 and 1");
        }

        if (game.getWinningMultiplier() <= 0) {
            throw new InvalidGameDataException(
                    "Invalid winning multiplier for game " + game.getName() +
                            ". Must be positive");
        }

        if (game.getMinBet() <= 0) {
            throw new InvalidGameDataException(
                    "Invalid min bet for game " + game.getName() +
                            ". Must be positive");
        }

        if (game.getMaxBet() <= game.getMinBet()) {
            throw new InvalidGameDataException(
                    "Max bet must be greater than min bet for game " + game.getName());
        }
    }

    @XmlRootElement(name = "games")
    public static class GameListWrapper {
        private List<Game> games;

        @XmlElement(name = "game")
        public List<Game> getGames() {
            return games;
        }

        public void setGames(List<Game> games) {
            this.games = games;
        }
    }
}