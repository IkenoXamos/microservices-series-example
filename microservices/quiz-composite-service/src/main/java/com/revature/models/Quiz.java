package com.revature.models;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class Quiz {

	private int id;
	private double grade;
	private String name;
	private List<Integer> cards;
	
	public boolean addCard(Integer id) {
		if(!cards.contains(id)) {
			return cards.add(id);
		}
		
		return false;
	}
	
	public boolean removeCard(Integer id) {
		if(cards.contains(id)) {
			return cards.remove(id);
		}
		
		return false;
	}
}
