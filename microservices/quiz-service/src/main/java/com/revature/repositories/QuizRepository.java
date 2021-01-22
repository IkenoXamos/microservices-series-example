package com.revature.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.revature.models.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {

	@Query(value = "SELECT DISTINCT q FROM Quiz q JOIN q.cards WHERE cards = ?1")
	public List<Quiz> findByCard(int id);
	
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "DELETE FROM QUIZ_CARDS WHERE CARDS = ?1")
	public void deleteCard(int id);
}
