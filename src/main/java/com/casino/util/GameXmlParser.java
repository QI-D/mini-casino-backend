package com.casino.util;

import com.casino.entity.Game;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class GameXmlParser {

    public static List<Game> parseXmlFile(MultipartFile file) throws Exception {
        JAXBContext context = JAXBContext.newInstance(GameListWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        GameListWrapper wrapper = (GameListWrapper) unmarshaller.unmarshal(file.getInputStream());
        return wrapper.getGames();
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