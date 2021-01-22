package com.revature.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.models.Flashcard;

public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {

	public List<Flashcard> findByIdIn(Integer[] ids);
}
