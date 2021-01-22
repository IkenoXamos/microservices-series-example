package com.revature.messaging;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.revature.messaging.FlashcardEvent;
import com.revature.models.Flashcard;
import com.revature.repositories.FlashcardRepository;

@Service
public class MessageService {

	private static Set<Integer> eventCache = new HashSet<>();
	
	@Autowired
	private FlashcardRepository flashcardDao;
	
	@Autowired
	private KafkaTemplate<String, FlashcardEvent> kt;
	
	public void triggerEvent(FlashcardEvent event) {
		eventCache.add(event.hashCode());
		
		if(event.getOperation() == Operation.DELETE) {
			kt.send("quiz-flashcard", event);
		}
		
		kt.send("flashcard", event);
	}
	
	@KafkaListener(topics = "flashcard")
	public void processFlashcardEvent(FlashcardEvent event) {
		
		if(eventCache.contains(event.hashCode())) {
			eventCache.remove(event.hashCode());
			return;
		}
		
		Flashcard f = event.getFlashcard();
		
		switch(event.getOperation()) {
		case CREATE:
			flashcardDao.save(f);
			break;
		case DELETE:
			flashcardDao.delete(f);
			break;
		}
	}
}
