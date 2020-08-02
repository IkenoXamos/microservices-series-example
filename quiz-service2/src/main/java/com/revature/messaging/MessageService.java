package com.revature.messaging;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.revature.messaging.FlashcardEvent;
import com.revature.messaging.QuizEvent;
import com.revature.repositories.QuizRepository;

@Service
public class MessageService {

	private static Set<Integer> eventCache = new HashSet<>();
	
	@Autowired
	private QuizRepository quizDao;
	
	@Autowired
	private KafkaTemplate<String, QuizEvent> kt;
	
	public void triggerEvent(QuizEvent event) {
		eventCache.add(event.hashCode());
		
		kt.send("quiz", event);
	}
	
	@KafkaListener(topics = "quiz")
	public void processQuizEvent(QuizEvent event) {
		
		if(eventCache.contains(event.hashCode())) {
			eventCache.remove(event.hashCode());
			return;
		}
		
		switch(event.getOperation()) {
		case CREATE:
			quizDao.save(event.getQuiz());
			break;
		case DELETE:
			break;
		}
	}
	
	@KafkaListener(topics = "quiz-flashcard")
	@Transactional
	public void processFlashcardEvent(FlashcardEvent event) {
		this.quizDao.deleteCard(event.getFlashcard().getId());
	}
}
