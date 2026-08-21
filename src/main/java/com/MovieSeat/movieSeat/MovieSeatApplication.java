package com.MovieSeat.movieSeat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MovieSeatApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieSeatApplication.class, args);

	}
}
