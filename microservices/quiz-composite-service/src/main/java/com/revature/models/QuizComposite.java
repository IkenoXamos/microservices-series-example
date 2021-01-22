package com.revature.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
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
