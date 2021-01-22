package com.revature.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.clients.FlashcardClient;
import com.revature.models.Flashcard;
import com.revature.models.Quiz;
import com.revature.repositories.QuizRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class QuizController {

	@Autowired
	private CircuitBreakerFactory<?, ?> cbFactory;

	@Autowired
	private QuizRepository quizDao;

	@Autowired
	private FlashcardClient flashcardClient;

	@GetMapping("/port")
	public ResponseEntity<String> retrievePort() {
		return this.cbFactory.create("flashcard-port").run(
				// Positive Case
				() -> {
					
					String port = this.flashcardClient.getPort();
					log.info("Request for flashcard port from quiz-service: {}", port);
					return ResponseEntity.ok(port);
				},

				// Negative Case
				throwable -> retrievePortFallback());
	}

	public ResponseEntity<String> retrievePortFallback() {
		log.error("flashcard-service is unavailable");
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("flashcard-service is currently unavailable. Please check back later.");
	}

	@GetMapping
	public ResponseEntity<List<Quiz>> findAll() {
		List<Quiz> all = quizDao.findAll();

		if (all.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		log.info("Request for all quizzes: {}", all);
		
		return ResponseEntity.ok(all);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Quiz> findById(@PathVariable("id") int id) {
		Optional<Quiz> optional = quizDao.findById(id);

		if (optional.isPresent()) {
			log.info("Request for quiz with id {}: {}", id, optional.get());
			
			return ResponseEntity.ok(optional.get());
		}
		
		log.info("Request for quiz with id {} but no match found for id", id);

		return ResponseEntity.noContent().build();
	}

	@PostMapping
	public ResponseEntity<Quiz> insert(@RequestBody Quiz quiz) {
		int id = quiz.getId();

		if (id != 0) {
			log.error("Request to create a new quiz failed: {}", quiz);
			
			return ResponseEntity.badRequest().build();
		}

		quizDao.save(quiz);
		
		log.info("Request to create a new quiz successfully completed: {}", quiz);
		
		return ResponseEntity.status(201).body(quiz);
	}

	@GetMapping("/cards")
	public ResponseEntity<List<Flashcard>> getCards() {
		return this.cbFactory.create("getCards").run(
				// Positive Case
				() -> {
					List<Flashcard> all = flashcardClient.findAll();

					if (all.isEmpty()) {
						return ResponseEntity.noContent().build();
					}
					
					log.info("Requested for flashcards from quiz-service: {}", all);

					return ResponseEntity.ok(all);
				},

				// Negative Case
				throwable -> getCardsFallback());
	}

	public ResponseEntity<List<Flashcard>> getCardsFallback() {
		log.error("flashcard-service is unavailable");
		
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.emptyList());
	}

	@GetMapping("/cards/{id}")
	public ResponseEntity<List<Flashcard>> getCardsFromQuiz(@PathVariable("id") int id) {
		return this.cbFactory.create("getCardsFromQuiz").run(

				// Positive Case
				() -> {
					Optional<Quiz> optional = this.quizDao.findById(id);

					if (optional.isPresent()) {
						Quiz q = optional.get();

						List<Integer> ids = q.getCards();
						
						List<Flashcard> cards = this.flashcardClient.findByIds(ids);
						
						log.info("Requested for flashcards for quiz with id {}: {}", id, cards);

						return ResponseEntity.ok(cards);
					}
					
					log.info("Requested for flashcards for quiz with id {}, but no quiz found", id);

					return ResponseEntity.badRequest().build();
				},

				// Negative Case
				throwable -> getCardsFromQuizFallback());
	}

	public ResponseEntity<List<Flashcard>> getCardsFromQuizFallback() {
		log.error("flashcard-service is unavailable");
		
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.emptyList());
	}
}
