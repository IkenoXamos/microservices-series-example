package com.revature.messaging;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.revature.models.Flashcard;
import com.revature.repositories.FlashcardRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService {
	
	private static Set<Integer> eventCache = new HashSet<>();
	
	@Autowired
	private FlashcardRepository flashcardDao;

	@Autowired
	private KafkaTemplate<String, FlashcardEvent> kt;
	
	public void triggerFlashcardEvent(FlashcardEvent event) {
		eventCache.add(event.hashCode());
		// This event has already been processed for THIS instance
		// So caching it will allow this instance to ignore the message when it receives it again
		
		if(event.getOperation() == Operation.DELETE) {
			kt.send("quiz-flashcard", event);
		}
		
		kt.send("flashcard", event);
		
		log.info("Messages sent to handle event: {}", event);
	}
	
	@KafkaListener(topics = "flashcard")
	public void processFlashcardEvent(FlashcardEvent event) {
		log.info("Message received to handle event: {}", event);
		
		if(eventCache.contains(event.hashCode())) {
			eventCache.remove(event.hashCode());
			
			log.info("Event has already been processed");
			
			return; // Quit Prematurely, to avoid re-processing event
		}
		
		Flashcard card = event.getFlashcard();
		
		switch(event.getOperation()) {
		case CREATE:
			this.flashcardDao.save(card);
			break;
		case UPDATE:
			this.flashcardDao.save(card);
			break;
		case DELETE:
			this.flashcardDao.deleteById(card.getId());
			break;
		}
	}
}
