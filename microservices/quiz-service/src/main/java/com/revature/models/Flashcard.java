package com.revature.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
public class Flashcard {
	
	private int id;
	private String question;
	private String answer;
	private String name;
	private Difficulty difficulty;
	private Topic topic;
}
