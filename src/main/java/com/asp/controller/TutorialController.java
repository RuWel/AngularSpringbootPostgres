package com.asp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asp.error.CustomError;
import com.asp.model.Tutorial;
import com.asp.repository.TutorialRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class TutorialController {

	@GetMapping("/hello")
	public ResponseEntity<String> sayHello() {
		return new ResponseEntity<>("Hello", HttpStatus.OK);
	}

/*	
	@Autowired
	TutorialRepository tutorialRepository;

	@GetMapping("/tutorials")
	public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String keyword) {
		List<Tutorial> result = new ArrayList<>();

		if (keyword == null ) {
			tutorialRepository.findAll().forEach(result::add);
		} else {
			tutorialRepository.findByTitleContaining(keyword).forEach(result::add);
		}
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> findTutorialByID (@PathVariable("id") Long id) {
		try {
			Optional<Tutorial> result = tutorialRepository.findById(id);
			
			if (result.isPresent()) {
				return (new ResponseEntity<>(result.get(), HttpStatus.FOUND));
			}
			
			return (new ResponseEntity<>(new CustomError("Tutorial with id " + id + " not found"), HttpStatus.NOT_FOUND));
		} catch (Exception e) {
			return (new ResponseEntity<>(new CustomError("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}
	
	// create tutorial
	@PostMapping(value="/tutorials")
	public ResponseEntity<Tutorial> createTutorial(@Valid @RequestBody Tutorial tutorial) {
		try {
			Tutorial result = tutorialRepository.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), tutorial.isPublished()));
		
			return (new ResponseEntity<>(result, HttpStatus.CREATED));
		} catch (Exception e) {
			return (new ResponseEntity<>(new CustomError("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}

	// update tutorial
	@PutMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") Long id, @Valid @RequestBody Tutorial tutorial) {
		try {
			Optional<Tutorial> result = tutorialRepository.findById(id);
			
			if (result.isPresent()) {
				Tutorial data = result.get();
				
				data.setTitle(tutorial.getTitle());
				data.setDescription(tutorial.getDescription());
				data.setPublished(tutorial.isPublished());
	
				Tutorial newEntity = tutorialRepository.save(data);
				
				return new ResponseEntity<>(newEntity, HttpStatus.OK);
			}
			
			return (new ResponseEntity<>(new CustomError("Tutorial with id " + id + " not found"), HttpStatus.NOT_FOUND));
		} catch (Exception e) {
			return (new ResponseEntity<>(new CustomError("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}

	// delete tutorial by ID
	@DeleteMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> deleteTutorial(@PathVariable("id") Long id) {
		try {
			tutorialRepository.deleteById(id);
			return (new ResponseEntity<>(HttpStatus.NO_CONTENT));
		} catch (Exception e) {
			return (new ResponseEntity<>(new CustomError("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}
	
	// delete all tutorials
	@DeleteMapping("/tutorials")
	public ResponseEntity<Tutorial> deleteAllTutorials() {
		try {
			tutorialRepository.deleteAll();
			return (new ResponseEntity<>(HttpStatus.NO_CONTENT));
		} catch (Exception e) {
			return (new ResponseEntity<>(new CustomError("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}

	// find published tutorials
	@GetMapping("/tutorials/published")
	public ResponseEntity<List<Tutorial>> findAllPublishedTutorials() {
		try {
			List<Tutorial> result = tutorialRepository.findByPublished(true);
			
			if (result.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
	
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
*/
}
