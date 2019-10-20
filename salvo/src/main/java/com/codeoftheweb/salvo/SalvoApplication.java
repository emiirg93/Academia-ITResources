package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository) {
        return (args) -> {
            Player player1 = playerRepository.save(new Player("j.bauer@ctu.gov"));
            Player player2 = playerRepository.save(new Player("c.obrian@ctu.gov"));

            Player player3 = playerRepository.save(new Player("t.almeida@ctu.gov"));
            Player player4 = playerRepository.save(new Player("d.palmer@whitehouse.gov"));

            Game game = gameRepository.save(new Game(LocalDateTime.parse("2019-08-03T18:20:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
            Game game2 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-05T10:12:20", DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
            Game game3 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-02T16:15:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
            Game game4 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-03T12:20:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
            Game game5 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-05T10:10:20", DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
            Game game6 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-02T11:18:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

            GamePlayer gamePlayer1 = gamePlayerRepository.save(new GamePlayer(game, player1, game.getCreationDate()));
            GamePlayer gamePlayer2 = gamePlayerRepository.save(new GamePlayer(game, player2, game.getCreationDate()));

            GamePlayer gamePlayer3 = gamePlayerRepository.save(new GamePlayer(game2, player2, game.getCreationDate()));
            GamePlayer gamePlayer4 = gamePlayerRepository.save(new GamePlayer(game2, player1, game.getCreationDate()));

            GamePlayer gamePlayer5 = gamePlayerRepository.save(new GamePlayer(game3, player3, game.getCreationDate()));
            GamePlayer gamePlayer6 = gamePlayerRepository.save(new GamePlayer(game3, player2, game.getCreationDate()));

            GamePlayer gamePlayer7 = gamePlayerRepository.save(new GamePlayer(game4, player1, game.getCreationDate()));
            GamePlayer gamePlayer8 = gamePlayerRepository.save(new GamePlayer(game4, player2, game.getCreationDate()));

            GamePlayer gamePlayer9 = gamePlayerRepository.save(new GamePlayer(game5, player3, game.getCreationDate()));
            GamePlayer gamePlayer10 = gamePlayerRepository.save(new GamePlayer(game5, player1, game.getCreationDate()));

            GamePlayer gamePlayer11 = gamePlayerRepository.save(new GamePlayer(game6, player4, game.getCreationDate()));
            GamePlayer gamePlayer12 = gamePlayerRepository.save(new GamePlayer(game6, player3, game.getCreationDate()));

            String[] var = {"H2", "H3", "H4"};
            String[] var2 = {"B5", "C5", "D5"};
            String[] var3 = {"E1", "F1", "G1"};
            List<String> position = new ArrayList<>();
            List<String> position2 = new ArrayList<>();
            List<String> position3 = new ArrayList<>();

            position.add(var[0]);
            position.add(var[1]);
            position.add(var[2]);

            position2.add(var2[0]);
            position2.add(var2[1]);
            position2.add(var2[2]);

            position3.add(var3[0]);
            position3.add(var3[1]);
            position3.add(var3[2]);

            shipRepository.save(new Ship("Destroyer", position, gamePlayer1));
            shipRepository.save(new Ship("Destroyer", position2, gamePlayer2));
            shipRepository.save(new Ship("Submarine", position3, gamePlayer1));

        };
    }
}
