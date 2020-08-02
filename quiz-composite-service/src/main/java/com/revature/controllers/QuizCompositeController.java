package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.revature.clients.FlashcardClient;
import com.revature.clients.QuizClient;
import com.revature.models.Flashcard;
import com.revature.models.Quiz;
import com.revature.models.QuizComposite;

@RestController
public class QuizCompositeController {

	@Autowired
	private FlashcardClient flashcardClient;
	
	@Autowired
	private QuizClient quizClient;
	
	@GetMapping("/{id}")
	public ResponseEntity<QuizComposite> compile(@PathVariable("id") int id) {
		Quiz q = this.quizClient.findById(id);
		
		List<Flashcard> cards = this.flashcardClient.findByIds(q.getCards());
		
		QuizComposite composite = new QuizComposite(q, cards);
		
		return ResponseEntity.ok(composite);
	}
}
