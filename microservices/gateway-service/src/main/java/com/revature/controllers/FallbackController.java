package com.revature.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/fallback")
public class FallbackController {

	@GetMapping
	public ResponseEntity<String> serviceDown() {
		log.error("Gateway unable to route request");
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("The service you have requested is currently unavailable. Please check back later.");
	}
	
	@GetMapping("/flashcard")
	public ResponseEntity<String> flashcardDown() {
		log.error("Gateway unable to route request to flashcard-service");
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("The flashcard service is currently unavailable. Please check back later.");
	}
}
