package com.revature.messaging;

import java.time.LocalDateTime;

import com.revature.models.Flashcard;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class FlashcardEvent {

	private Flashcard flashcard;
	private Operation operation;
	private LocalDateTime timestamp;
	
	public FlashcardEvent(Flashcard flashcard, Operation operation) {
		this.flashcard = flashcard;
		this.operation = operation;
		this.timestamp = LocalDateTime.now();
	}
}
