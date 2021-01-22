package com.revature.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class Flashcard {
	
	private int id;
	private String question;
	private String answer;
	private String name;
	private Difficulty difficulty;
	private Topic topic;
}
