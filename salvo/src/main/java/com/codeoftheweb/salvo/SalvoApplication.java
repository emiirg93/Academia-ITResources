package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Ship;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ShipRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);

	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository) {
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

			shipRepository.save(new Ship("Destroyer",gp1,Ship.ApplicationLocations("H2","H3","H4")));
			shipRepository.save(new Ship("Submarine",gp1,Ship.ApplicationLocations("E1","F1","G1")));
			shipRepository.save(new Ship("Patrol Boat",gp1,Ship.ApplicationLocations("B4","B5")));

			shipRepository.save(new Ship("Destroyer",gp2,Ship.ApplicationLocations("B5","C5","D5")));
			shipRepository.save(new Ship("Patrol Boat",gp2,Ship.ApplicationLocations("F1","F2")));

			shipRepository.save(new Ship("Destroyer",gp3,Ship.ApplicationLocations("B5","C5","D5")));
			shipRepository.save(new Ship("Patrol Boat",gp3,Ship.ApplicationLocations("C6","C7")));

			shipRepository.save(new Ship("Submarine",gp4,Ship.ApplicationLocations("A2","A3","A4")));
			shipRepository.save(new Ship("Patrol Boat",gp4,Ship.ApplicationLocations("G6","H6")));

			shipRepository.save(new Ship("Destroyer",gp5,Ship.ApplicationLocations("B5","C5","D5")));
			shipRepository.save(new Ship("Patrol Boat",gp5,Ship.ApplicationLocations("C6","C7")));

			shipRepository.save(new Ship("Submarine",gp6,Ship.ApplicationLocations("A2","A3","A4")));
			shipRepository.save(new Ship("Patrol Boat",gp6,Ship.ApplicationLocations("G6","H6")));

			shipRepository.save(new Ship("Destroyer",gp7,Ship.ApplicationLocations("B5","C5","D5")));
			shipRepository.save(new Ship("Patrol Boat",gp7,Ship.ApplicationLocations("C6","C7")));

			shipRepository.save(new Ship("Submarine",gp8,Ship.ApplicationLocations("A2","A3","A4")));
			shipRepository.save(new Ship("Patrol Boat",gp8,Ship.ApplicationLocations("G6","H6")));

			shipRepository.save(new Ship("Destroyer",gp9,Ship.ApplicationLocations("B5","C5","D5")));
			shipRepository.save(new Ship("Patrol Boat",gp9,Ship.ApplicationLocations("C6","C7")));

			shipRepository.save(new Ship("Submarine",gp8,Ship.ApplicationLocations("A2","A3","A4")));
			shipRepository.save(new Ship("Patrol Boat",gp8,Ship.ApplicationLocations("G6","H6")));

		};

	}
}
