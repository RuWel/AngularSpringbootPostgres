package com.asp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asp.model.Tutorial;

public interface TutorialRepository extends JpaRepository<Tutorial, Long>{
	List<Tutorial> findByPublished(boolean isPublished);
	List<Tutorial> findByTitleContaining(String keyword);
}