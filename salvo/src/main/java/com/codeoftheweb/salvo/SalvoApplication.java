package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.*;
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
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
		return (args) -> {
			// save a couple of customers
			Player player1 = playerRepository.save(new Player("Jack Bauer","j.bauer@ctu.gov"));
			Player player2 = playerRepository.save(new Player("Chloe O'Brian","c.obrian@ctu.gov"));
			Player player3 = playerRepository.save(new Player("Tony Almeida","t.almeida@ctu.gov"));
			Player player4 = playerRepository.save(new Player("Diego Palmer","d.palmer@whitehouse.gov"));
//			Player player5 = playerRepository.save(new Player("Kim Bauer","kim_bauer@itresources.gov"));

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			Game game1 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-05 08:35:26",formatter)));
			Game game2 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-05 10:12:20", formatter)));
			Game game3 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-02 16:15:00", formatter)));
			Game game4 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-03 12:20:00", formatter)));
			Game game5 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-05 10:10:20", formatter)));
			Game game6 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-02 11:18:00", formatter)));
//			Game game7 = gameRepository.save(new Game(LocalDateTime.parse("2019-08-10 18:20:00", formatter)));

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
			Ship s1 = shipRepository.save(new Ship("Patrol Boat",gp1,Ship.ApplicationLocations("B4","B5")));

			shipRepository.save(new Ship("Destroyer",gp2,Ship.ApplicationLocations("B5","C5","D5")));
			Ship s2 =shipRepository.save(new Ship("Patrol Boat",gp2,Ship.ApplicationLocations("F1","F2")));

			shipRepository.save(new Ship("Destroyer",gp3,Ship.ApplicationLocations("B5","C5","D5")));
			Ship s3 =shipRepository.save(new Ship("Patrol Boat",gp3,Ship.ApplicationLocations("C6","C7")));

			shipRepository.save(new Ship("Submarine",gp4,Ship.ApplicationLocations("A2","A3","A4")));
			Ship s4 =shipRepository.save(new Ship("Patrol Boat",gp4,Ship.ApplicationLocations("G6","H6")));

			shipRepository.save(new Ship("Destroyer",gp5,Ship.ApplicationLocations("B5","C5","D5")));
			Ship s5 =shipRepository.save(new Ship("Patrol Boat",gp5,Ship.ApplicationLocations("C6","C7")));

			shipRepository.save(new Ship("Submarine",gp6,Ship.ApplicationLocations("A2","A3","A4")));
			Ship s6 =shipRepository.save(new Ship("Patrol Boat",gp6,Ship.ApplicationLocations("G6","H6")));

			shipRepository.save(new Ship("Destroyer",gp7,Ship.ApplicationLocations("B5","C5","D5")));
			Ship s7 =shipRepository.save(new Ship("Patrol Boat",gp7,Ship.ApplicationLocations("C6","C7")));

			shipRepository.save(new Ship("Submarine",gp8,Ship.ApplicationLocations("A2","A3","A4")));
			Ship s8 =shipRepository.save(new Ship("Patrol Boat",gp8,Ship.ApplicationLocations("G6","H6")));

			shipRepository.save(new Ship("Destroyer",gp9,Ship.ApplicationLocations("B5","C5","D5")));
			Ship s9 =shipRepository.save(new Ship("Patrol Boat",gp9,Ship.ApplicationLocations("C6","C7")));

			shipRepository.save(new Ship("Submarine",gp8,Ship.ApplicationLocations("A2","A3","A4")));
			Ship s10 =shipRepository.save(new Ship("Patrol Boat",gp8,Ship.ApplicationLocations("G6","H6")));

			Salvo salvo1 = salvoRepository.save(new Salvo(s1.getGamePlayer(),1,Salvo.ApplicationLocations("B5","C5","F1")));
			Salvo salvo2 = salvoRepository.save(new Salvo(s2.getGamePlayer(),1,Salvo.ApplicationLocations("B4","B5","B6")));

			Salvo salvo3 = salvoRepository.save(new Salvo(s1.getGamePlayer(),2,Salvo.ApplicationLocations("F2","D5")));
			Salvo salvo4 = salvoRepository.save(new Salvo(s2.getGamePlayer(),2,Salvo.ApplicationLocations("E1","E3","A4")));

			Salvo salvo5 = salvoRepository.save(new Salvo(s3.getGamePlayer(),1,Salvo.ApplicationLocations("A2","A4","G6")));
			Salvo salvo6 = salvoRepository.save(new Salvo(s4.getGamePlayer(),1,Salvo.ApplicationLocations("B5","D5","C7")));

			Salvo salvo7 = salvoRepository.save(new Salvo(s3.getGamePlayer(),2,Salvo.ApplicationLocations("A3","H6")));
			Salvo salvo8 = salvoRepository.save(new Salvo(s4.getGamePlayer(),2,Salvo.ApplicationLocations("C5","C6")));

			Salvo salvo9 = salvoRepository.save(new Salvo(s5.getGamePlayer(),1,Salvo.ApplicationLocations("G6","H6","A4")));
			Salvo salvo10 = salvoRepository.save(new Salvo(s6.getGamePlayer(),1,Salvo.ApplicationLocations("H1","H2","H3")));

			Salvo salvo11 = salvoRepository.save(new Salvo(s5.getGamePlayer(),2,Salvo.ApplicationLocations("A2","A3","D8")));
			Salvo salvo12 = salvoRepository.save(new Salvo(s6.getGamePlayer(),2,Salvo.ApplicationLocations("E1","F2","G3")));

			Salvo salvo13 = salvoRepository.save(new Salvo(s7.getGamePlayer(),1,Salvo.ApplicationLocations("A3","A4","F7")));
			Salvo salvo14 = salvoRepository.save(new Salvo(s8.getGamePlayer(),1,Salvo.ApplicationLocations("B5","C6","H1")));

			Salvo salvo15 = salvoRepository.save(new Salvo(s7.getGamePlayer(),2,Salvo.ApplicationLocations("A2","G6","H6")));
			Salvo salvo16 = salvoRepository.save(new Salvo(s8.getGamePlayer(),2,Salvo.ApplicationLocations("C5","C7","D5")));

			Salvo salvo17 = salvoRepository.save(new Salvo(s9.getGamePlayer(),1,Salvo.ApplicationLocations("A1","A2","A3")));
			Salvo salvo18 = salvoRepository.save(new Salvo(s10.getGamePlayer(),1,Salvo.ApplicationLocations("B5","B6","C7")));

			Salvo salvo19 = salvoRepository.save(new Salvo(s9.getGamePlayer(),2,Salvo.ApplicationLocations("G6","G7","G8")));
			Salvo salvo20 = salvoRepository.save(new Salvo(s10.getGamePlayer(),2,Salvo.ApplicationLocations("C6","D6","E7")));




			scoreRepository.save(new Score(game1,player1,1.0,game1.getCreationDate()));
			scoreRepository.save(new Score(game2,player1,1.0,game1.getCreationDate()));
			scoreRepository.save(new Score(game3,player1,0.5,game1.getCreationDate()));
			scoreRepository.save(new Score(game4,player1,0.0,game1.getCreationDate()));

			scoreRepository.save(new Score(game1,player2,1.0,game1.getCreationDate()));
			scoreRepository.save(new Score(game2,player2,1.0,game1.getCreationDate()));
			scoreRepository.save(new Score(game3,player2,1.0,game1.getCreationDate()));
			scoreRepository.save(new Score(game4,player2,0.5,game1.getCreationDate()));
		};

	}
}
