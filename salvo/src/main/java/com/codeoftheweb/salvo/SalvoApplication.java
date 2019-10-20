package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
		return (args) -> {
			// save a couple of customers
			Player player1 = playerRepository.save(new Player("j.bauer@ctu.gov"));
			Player player2 = playerRepository.save(new Player("c.obrian@ctu.gov"));
			Player player3 = playerRepository.save(new Player("t.almeida@ctu.gov"));
			Player player4 = playerRepository.save(new Player("d.palmer@whitehouse.gov"));

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			Game game1 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-05 08:35:26",formatter)));
			Game game2 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-05 10:12:20", formatter)));
			Game game3 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-02 16:15:00", formatter)));
			Game game4 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-03 12:20:00", formatter)));
			Game game5 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-05 10:10:20", formatter)));
			Game game6 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-02 11:18:00", formatter)));

			GamePlayer gp1 = gamePlayerRepository.save(new GamePlayer(game1,player1,game1.getCreationDate()));
			GamePlayer gp2 = gamePlayerRepository.save(new GamePlayer(game1,player2,game1.getCreationDate()));

			GamePlayer gp3 = gamePlayerRepository.save(new GamePlayer(game2,player1,game2.getCreationDate()));
			GamePlayer gp4 = gamePlayerRepository.save(new GamePlayer(game2,player2,game2.getCreationDate()));

			GamePlayer gp5 = gamePlayerRepository.save(new GamePlayer(game3,player2,game3.getCreationDate()));
			GamePlayer gp6 = gamePlayerRepository.save(new GamePlayer(game3,player3,game3.getCreationDate()));

			GamePlayer gp7 = gamePlayerRepository.save(new GamePlayer(game4,player1,game4.getCreationDate()));
			GamePlayer gp8 = gamePlayerRepository.save(new GamePlayer(game4,player2,game4.getCreationDate()));

			GamePlayer gp9 = gamePlayerRepository.save(new GamePlayer(game5,player3,game5.getCreationDate()));
			GamePlayer gp10 = gamePlayerRepository.save(new GamePlayer(game5,player1,game5.getCreationDate()));

			GamePlayer gp11 = gamePlayerRepository.save(new GamePlayer(game6,player4,game6.getCreationDate()));


		};
	}
}
