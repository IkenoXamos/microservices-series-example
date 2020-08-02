package com.revature.messaging;

import java.time.LocalDateTime;

import com.revature.models.Quiz;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
public class QuizEvent {
	
	private Operation operation;
	private Quiz quiz;
	private LocalDateTime timestamp;
	
	public QuizEvent(Operation operation, Quiz quiz) {
		this.operation = operation;
		this.quiz = quiz;
		this.timestamp = LocalDateTime.now();
	}
}
