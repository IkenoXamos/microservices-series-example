package com.revature.models;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor
public class Quiz {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private double grade;
	private String name;
	
	@ElementCollection
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
