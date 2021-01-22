package com.revature.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.revature.models.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "DELETE FROM QUIZ_CARDS WHERE CARDS = :id")
	public void deleteCard(int id);
}
