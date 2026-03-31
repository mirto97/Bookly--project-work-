package com.generation.bookly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // Abilita l'esecuzione asincrona per i metodi annotati con @Async (es. invio mail)
public class BooklyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooklyApplication.class, args);
	}
}
