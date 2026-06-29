package com.example.High_Concurrency_Ticketing_API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableRetry
@EnableCaching
@EnableAsync

public class HighConcurrencyTicketingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HighConcurrencyTicketingApiApplication.class, args);
	}

}
