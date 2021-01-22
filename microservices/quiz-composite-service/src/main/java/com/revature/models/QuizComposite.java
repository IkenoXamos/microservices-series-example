package com.revature.models;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class QuizComposite {

	private int id;
	private double grade;
	private String name;
	private List<Flashcard> cards;
	
	public QuizComposite(Quiz q, List<Flashcard> cards) {
		this.id = q.getId();
		this.grade = q.getGrade();
		this.name = q.getName();
		this.cards = cards;
	}
}
