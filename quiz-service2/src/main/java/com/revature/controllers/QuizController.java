package com.revature.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.clients.FlashcardClient;
import com.revature.messaging.MessageService;
import com.revature.messaging.Operation;
import com.revature.messaging.QuizEvent;
import com.revature.models.Flashcard;
import com.revature.models.Quiz;
import com.revature.repositories.QuizRepository;

@RestController
public class QuizController {

	@Autowired
	private QuizRepository quizDao;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private FlashcardClient flashcardClient;
	
	@GetMapping("/port")
	public String retrievePort() {
		String port = this.flashcardClient.retrievePort();
		
		return port;
	}
	
	@GetMapping
	public ResponseEntity<List<Quiz>> findAll() {
		List<Quiz> all = quizDao.findAll();
		
		if(all.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(all);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Quiz> findById(@PathVariable("id") int id) {
		Optional<Quiz> optional = quizDao.findById(id);
		
		if(optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping
	public ResponseEntity<Quiz> insert(@RequestBody Quiz quiz) {
		int id = quiz.getId();
		
		if(id != 0) {
			return ResponseEntity.badRequest().build();
		}
		
		this.quizDao.save(quiz);
		this.messageService.triggerEvent(new QuizEvent(Operation.CREATE, quiz));
		
		return ResponseEntity.status(201).body(quiz);
	}
	
	@GetMapping("/cards")
	public ResponseEntity<List<Flashcard>> getCards() {
		List<Flashcard> all = this.flashcardClient.findAll(); // Could be time consuming
		
		if(all.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(all);
	}
	
	@GetMapping("/cards/{id}")
	public ResponseEntity<List<Flashcard>> getCardsFromQuiz(@PathVariable("id") int id) {
		Optional<Quiz> optional = this.quizDao.findById(id);
		
		if(optional.isPresent()) {
			List<Integer> ids = optional.get().getCards();
			return ResponseEntity.ok(this.flashcardClient.findByIds(ids));
		}
		
		return ResponseEntity.noContent().build();
	}
}
