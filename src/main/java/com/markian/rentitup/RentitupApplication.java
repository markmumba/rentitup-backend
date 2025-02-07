package com.markian.rentitup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RentitupApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentitupApplication.class, args);
	}

}
