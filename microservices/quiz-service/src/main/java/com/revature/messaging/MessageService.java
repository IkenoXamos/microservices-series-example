package com.revature.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.revature.models.Flashcard;
import com.revature.repositories.QuizRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService {
	
	@Autowired
	private QuizRepository quizDao;

	@KafkaListener(topics = "quiz-flashcard")
	public void processFlashcardEvent(FlashcardEvent event) {
		log.info("Message received to handle event: {}", event);
		
		switch(event.getOperation()) {
		case DELETE:
			
			
			
			Flashcard removed = event.getFlashcard();
			this.quizDao.deleteCard(removed.getId());
			
			log.info("Removed flashcards with id {} from all quizzes", removed.getId());
			break;
		default:
			break;
		}
	}
}
