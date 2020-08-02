package com.revature.messaging;

import java.time.LocalDateTime;

import com.revature.models.Flashcard;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
public class FlashcardEvent {
	private Operation operation;
	private Flashcard flashcard;
	private LocalDateTime timestamp;
}
