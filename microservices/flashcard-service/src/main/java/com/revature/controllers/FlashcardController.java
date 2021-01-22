package com.revature.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.messaging.FlashcardEvent;
import com.revature.messaging.MessageService;
import com.revature.messaging.Operation;
import com.revature.models.Flashcard;
import com.revature.repositories.FlashcardRepository;

@RestController
public class FlashcardController {
	
	@Autowired
	private FlashcardRepository flashcardDao;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private Environment env;
	
	@GetMapping("/port")
	public String getPort() {
		String port = env.getProperty("local.server.port");
		
		return "Hello, this came from port " + port;
	}

	@GetMapping
	public ResponseEntity<List<Flashcard>> findAll(@RequestParam(required = false) Integer[] ids) {
		if(ids == null) {
			List<Flashcard> all = flashcardDao.findAll();
			
			return ResponseEntity.ok(all);
		}
		
		List<Flashcard> some = flashcardDao.findByIdIn(Arrays.asList(ids));
		
		if(some.size() != ids.length) {
			return ResponseEntity.badRequest().body(some);
		}
		
		return ResponseEntity.ok(some);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Flashcard> findById(@PathVariable("id") int id) {
		Optional<Flashcard> optional = flashcardDao.findById(id);
		
		if(optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping
	public ResponseEntity<Flashcard> insert(@RequestBody Flashcard flashcard) {
		int id = flashcard.getId();
		
		if(id != 0) {
			return ResponseEntity.badRequest().build();
		}
		
		flashcardDao.save(flashcard);
		
		FlashcardEvent event = new FlashcardEvent(Operation.CREATE, flashcard);
		this.messageService.triggerEvent(event);
		
		return ResponseEntity.status(201).body(flashcard);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Flashcard> delete(@PathVariable("id") int id) {
		Optional<Flashcard> option = flashcardDao.findById(id);

		if(option.isPresent()) {
			Flashcard f = option.get();
			flashcardDao.delete(f);
			
			FlashcardEvent event = new FlashcardEvent(Operation.DELETE, f);
			this.messageService.triggerEvent(event);
			
			return ResponseEntity.accepted().body(f);
		}
		
		return ResponseEntity.notFound().build();
	}
}
