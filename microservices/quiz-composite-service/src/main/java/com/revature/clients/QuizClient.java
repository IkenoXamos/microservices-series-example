package com.revature.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.revature.models.Quiz;

@FeignClient(name = "quiz")
public interface QuizClient {
	
	@GetMapping
	public List<Quiz> findAll();

	@GetMapping("/{id}")
	public Quiz findById(@PathVariable("id") int id);
}
