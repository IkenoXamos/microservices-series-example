package com.revature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FlashcardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlashcardServiceApplication.class, args);
	}
}
