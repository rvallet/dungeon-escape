package com.rva.dungeon;

import com.rva.dungeon.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DungeonEscapeApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(DungeonEscapeApplication.class, args);

		GameService gameService = context.getBean(GameService.class);
		gameService.startGame();

	}

}
