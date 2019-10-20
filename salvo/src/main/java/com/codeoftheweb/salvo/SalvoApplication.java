package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository) {
		return (args) -> {
			// save a couple of customers
			Player player1 = repository.save(new Player("j.bauer@ctu.gov"));
			Player player2 = repository.save(new Player("c.obrian@ctu.gov"));
			Player player3 = repository.save(new Player("t.almeida@ctu.gov"));
			Player player4 = repository.save(new Player("d.palmer@whitehouse.gov"));
		};
	}
}
