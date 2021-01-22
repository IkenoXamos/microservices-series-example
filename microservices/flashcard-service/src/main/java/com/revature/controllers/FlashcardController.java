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

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		
		log.info("Request for port: {}", port);
		
		return "Hello, this came from port " + port;
	}

	@GetMapping
	public ResponseEntity<List<Flashcard>> findAll(@RequestParam(required = false) Integer[] ids) {
		if(ids == null) {
			List<Flashcard> all = flashcardDao.findAll();
			
			log.info("Request for all flashcards: {}", all);
			
			return ResponseEntity.ok(all);
		}
		
		List<Flashcard> some = this.flashcardDao.findByIdIn(ids);
		
		if(some.size() != ids.length) {
			log.warn("Requested ids did not match the list of ids found. {} ids found vs {} ids requested", some.size(), ids.length);
			
			return ResponseEntity.badRequest().body(some);
		}
		
		log.info("Request for flashcards with ids {}: {}", Arrays.toString(ids), some);
		
		return ResponseEntity.ok(some);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Flashcard> findById(@PathVariable("id") int id) {
		Optional<Flashcard> optional = flashcardDao.findById(id);
		
		if(optional.isPresent()) {
			log.info("Request for flashcard with id {}: {}", id, optional.get());
			
			return ResponseEntity.ok(optional.get());
		}
		
		log.info("Request for flashcard with id {}, but no match found for id");
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping
	public ResponseEntity<Flashcard> insert(@RequestBody Flashcard flashcard) {
		int id = flashcard.getId();
		
		if(id != 0) {
			log.error("Request to create a new flashcard failed: {}", flashcard);
			
			return ResponseEntity.badRequest().build();
		}
		
		flashcardDao.save(flashcard);
		this.messageService.triggerFlashcardEvent(new FlashcardEvent(flashcard, Operation.CREATE));
		
		log.info("Request to create a new flashcard successfully completed: {}", flashcard);
		
		return ResponseEntity.status(201).body(flashcard);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Flashcard> delete(@PathVariable("id") int id) {
		Optional<Flashcard> option = flashcardDao.findById(id);

		if(option.isPresent()) {
			flashcardDao.delete(option.get());
			this.messageService.triggerFlashcardEvent(new FlashcardEvent(option.get(), Operation.DELETE));
			
			log.info("Request to delete flashcard with id {} successfully completed");
			return ResponseEntity.accepted().body(option.get());
		}
		
		log.info("Request to delete flashcard with id {}, but no match found for id");
		
		return ResponseEntity.notFound().build();
	}
}
