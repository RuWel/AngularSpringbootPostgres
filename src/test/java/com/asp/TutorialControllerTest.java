package com.asp;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.asp.controller.TutorialController;
import com.asp.error.CustomError;
import com.asp.model.Tutorial;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TutorialControllerTest {

	@LocalServerPort
	private int port;
		
	@Autowired
	private TutorialController tutorialController;

	@Autowired
	private TestRestTemplate testRestTemplate;

	private Long id1;
	
	private String url;
	
	@BeforeEach
	void setupDatabaseForTest() {
		url = "http://localhost:" + port + "/api/tutorials";
		
		tutorialController.deleteAllTutorials();
	
		Tutorial input = new Tutorial("Hello World", "testdescription1", false);
		ResponseEntity<Tutorial> result = this.tutorialController.createTutorial(input);
		id1 = result.getBody().getId();

		input = new Tutorial("Miss World", "testdescription2", false);
		result = this.tutorialController.createTutorial(input);
	}
	
	@Test
	void contextLoads() throws Exception {
		assertThat(tutorialController).isNotNull();
	}
	
	@Test
	void getAllTutorialsTest() {
		assertThat(this.tutorialController.getAllTutorials(null)).isNotNull();
	}
	
	@Test
	void findTutorialByIDTest() {
		ResponseEntity<Tutorial> result = this.tutorialController.findTutorialByID(id1);
		
		Assertions.assertEquals(HttpStatus.FOUND, result.getStatusCode());
		
		assertThat(result.getBody().getId()).isEqualTo(id1);
		assertThat(result.getBody().getTitle()).isEqualTo("Hello World");
		assertThat(result.getBody().getDescription()).isEqualTo("testdescription1");
		assertThat(result.getBody().isPublished()).isFalse();
	}
	
	@Test
	void findTutorialByInvalidIDTest() {
		ResponseEntity<Tutorial> result = this.tutorialController.findTutorialByID(-1L);
		
		Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		
		if (result.getBody() != null) {
			if (result.getBody() instanceof CustomError ce) {
				assertThat(ce.getErrorMessage()).isNotEmpty();
			}
		} else {
			assertThat(result.getBody()).isNull();
		}
	}
	
	@Test
	void findTutorialsByKeywordHelloTest () {
		ResponseEntity<List<Tutorial>> result = this.tutorialController.getAllTutorials("Hello");
		Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
		Assertions.assertEquals(1, result.getBody().size());

		Tutorial tutorial = result.getBody().get(0);
		assertThat(tutorial.getTitle().contains("Hello"));
	}
	
	@Test
	void findTutorialsByKeywordWorldTest () {
		ResponseEntity<List<Tutorial>> result = this.tutorialController.getAllTutorials("World");		
		Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
		Assertions.assertEquals(2, result.getBody().size());

		Tutorial tutorial = result.getBody().get(0);
		assertThat(tutorial.getTitle().contains("World"));
		
		tutorial = result.getBody().get(1);
		assertThat(tutorial.getTitle().contains("World"));
	}

	@Test
	void findTutorialsByKeywordNoMatchTest () {
		ResponseEntity<List<Tutorial>> result = this.tutorialController.getAllTutorials("XXX");		
		Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
		Assertions.assertEquals(0, result.getBody().size());
	}

	@Test
	void createTutorialTest() {
		Tutorial tutorial = new Tutorial("testtitle3", "testdescription3", false);
		
		ResponseEntity<Tutorial> result = this.tutorialController.createTutorial(tutorial);
		
		Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());

		assertThat(result.getBody().getId()).isNotEqualTo(0);
		assertThat(result.getBody().getTitle()).isEqualTo("testtitle3");
		assertThat(result.getBody().getDescription()).isEqualTo("testdescription3");
		assertThat(result.getBody().isPublished()).isFalse();
	}

	@Test
	void createTutorialEmptyTitleTest() {
		Tutorial tutorial = new Tutorial("", "testdescription'", false);
		
		ResponseEntity<Tutorial> result = this.tutorialController.createTutorial(tutorial);
		
		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());

		if (result.getBody() != null) {
			if (result.getBody() instanceof CustomError ce) {
				assertThat(ce.getErrorMessage()).isNotEmpty();
			}
		} else {
			assertThat(result.getBody()).isNull();
		}
	}

	@Test
	void createTutorialTooLongDescriptionTest() {
		Tutorial tutorial = new Tutorial("testtitle5", "testdescription5testdescription5testdescription5testdescription5testdescription5testdescription5testdescription5testdescription5testdescription5testdescription5testdescription5testdescription5testdescription5testdescription5testdescription5testdescription5testdescription5testdescription5testdescription5testdescription5", false);
		
		ResponseEntity<Tutorial> result = this.tutorialController.createTutorial(tutorial);
		
		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());

		if (result.getBody() != null) {
			if (result.getBody() instanceof CustomError ce) {
				assertThat(ce.getErrorMessage()).isNotEmpty();
			}
		} else {
			assertThat(result.getBody()).isNull();
		}
	}

	@Test
	void createTutorialTestWithException() {
		Tutorial input = new Tutorial("", "testdescription4", false);

		ResponseEntity<Tutorial> result = this.tutorialController.createTutorial(input);
		
		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
	}

	@Test
	void updateTutorialTest() {
		Tutorial tutorial = new Tutorial("testtitle_1", "testdescription_1", false);

		ResponseEntity<Tutorial> result = this.tutorialController.updateTutorial(id1, tutorial);
		assertThat(HttpStatus.OK.equals(result.getStatusCode()));

		assertThat(tutorial.getTitle()).isEqualTo(result.getBody().getTitle());
		assertThat(tutorial.getDescription()).isEqualTo(result.getBody().getDescription());
		assertThat(tutorial.isPublished()).isEqualTo(result.getBody().isPublished());
	}

	@Test
	void updateTutorialEmptyTitleTest() {
		Tutorial tutorial = new Tutorial("", "testdescription_1", false);

		ResponseEntity<Tutorial> result = this.tutorialController.updateTutorial(id1, tutorial);
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR.equals(result.getStatusCode()));

		if (result.getBody() != null) {
			if (result.getBody() instanceof CustomError ce) {
				assertThat(ce.getErrorMessage()).isNotEmpty();
			}
		} else {
			assertThat(result.getBody()).isNull();
		}
	}

	@Test
	void updateTutorialLongDescriptionTest() {
		Tutorial tutorial = new Tutorial("testtitle_1", "testdescription_1testdescription_1testdescription_1testdescription_1testdescription_1testdescription_1testdescription_1testdescription_1testdescription_1testdescription_1testdescription_1testdescription_1testdescription_1testdescription_1testdescription_1testdescription_1", false);

		ResponseEntity<Tutorial> result = this.tutorialController.updateTutorial(id1, tutorial);
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR.equals(result.getStatusCode()));

		if (result.getBody() != null) {
			if (result.getBody() instanceof CustomError ce) {
				assertThat(ce.getErrorMessage()).isNotEmpty();
			}
		} else {
			assertThat(result.getBody()).isNull();
		}
	}

	@Test
	void updateTutorialNotFoundTest() {
		Tutorial tutorial = new Tutorial("testtitle_1", "testdescription_1", false);

		ResponseEntity<Tutorial> response = this.tutorialController.updateTutorial(-1L, tutorial);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void deleteTutorialTest() {
		ResponseEntity<Tutorial> response = this.tutorialController.deleteTutorial(id1);
		Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	void deleteTutorialNotFoundTest() {
		ResponseEntity<Tutorial> response = this.tutorialController.deleteTutorial(-1L);
		Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	void deleteAllTutorialTest() {
		ResponseEntity<Tutorial> response = this.tutorialController.deleteAllTutorials();
		Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	void findAllPublishedTutorialNonePublishedTest() {
		ResponseEntity<List<Tutorial>> response = this.tutorialController.findAllPublishedTutorials();
		Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	void findAllPublishedTutorialOnePublishedTest() {
		Tutorial tutorial = new Tutorial("anewone", "testdescription", true);
		this.tutorialController.createTutorial(tutorial);
		
		ResponseEntity<List<Tutorial>> response = this.tutorialController.findAllPublishedTutorials();
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void createTutorialRTTest() {
		Tutorial tutorial = new Tutorial("testtitle3", "testdescription3", false);
		
		ResponseEntity<Tutorial> result = this.testRestTemplate.postForEntity(url, tutorial, Tutorial.class);

		Assertions.assertEquals(HttpStatus.CREATED.value(), result.getStatusCode().value());

		assertThat(result.getBody().getId()).isNotEqualTo(0);
		assertThat(result.getBody().getTitle()).isEqualTo("testtitle3");
		assertThat(result.getBody().getDescription()).isEqualTo("testdescription3");
		assertThat(result.getBody().isPublished()).isFalse();
	}

	@Test
	void getAllTutorialsRTTest() {
		assertThat(this.testRestTemplate.getForObject(url, List.class)).isNotNull();
	}

	@Test
	void findTutorialByIDRTTest() {
		ResponseEntity<Tutorial> result = this.testRestTemplate.getForEntity(url + "/" + id1, Tutorial.class);
		
		Assertions.assertEquals(HttpStatus.FOUND, result.getStatusCode());
		
		assertThat(result.getBody().getId()).isEqualTo(id1);
		assertThat(result.getBody().getTitle()).isEqualTo("Hello World");
		assertThat(result.getBody().getDescription()).isEqualTo("testdescription1");
		assertThat(result.getBody().isPublished()).isFalse();
	}

	@Test
	void findTutorialByInvalidIDRTTest() {
		ResponseEntity<Tutorial> result = this.testRestTemplate.getForEntity(url + "/" + -1L, Tutorial.class);
		
		Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		
		if (result.getBody() != null) {
			if (result.getBody() instanceof CustomError ce) {
				assertThat(ce.getErrorMessage()).isNotEmpty();
			}
		} else {
			assertThat(result.getBody()).isNull();
		}
	}

	@Test
	void findTutorialsByKeywordHelloRTTest () {
		URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("keyword", "Hello").build().toUri();
				
		ResponseEntity<List> result = this.testRestTemplate.getForEntity(uri, List.class);		
		LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>)(result.getBody().get(0));
		
		Assertions.assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
		
		String title = (String)map.get("title");
		
		assertThat(title.contains("Hello"));
		Assertions.assertEquals(1, result.getBody().size());
	}

	@Test
	void findTutorialsByKeywordWorldRTTest () {
		URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("keyword", "World").build().toUri();

		ResponseEntity<List> result = this.testRestTemplate.getForEntity(uri, List.class);		
		LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>)(result.getBody().get(0));

		Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
		Assertions.assertEquals(2, result.getBody().size());

		String title = (String)map.get("title");
		
		assertThat(title.contains("World"));
	}

	@AfterEach
	void cleanupDatabaseAfterTest() {
		tutorialController.deleteAllTutorials();
	}
}
