package com.generation.bookly;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BooklyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooklyApplication.class, args);
	}

	@Bean
	public CommandLineRunner runDataSeeder(DataSeeder seeder) {
		return args -> {
			System.out.println("Inizio DataSeeder all'avvio di Spring Boot");
			seeder.run(args);
			System.out.println("DataSeeder completato");
		};
	}
}
