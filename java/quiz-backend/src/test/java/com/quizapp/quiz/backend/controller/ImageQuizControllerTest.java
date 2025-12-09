package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Game;
import com.quizapp.quiz.backend.service.ImageQuizService;
import com.quizapp.quiz.backend.service.ImageQuizService.ComparisonResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ImageQuizController} class.
 * <p>
 * This class validates the endpoints responsible for the "Image Quiz" mode.
 * It tests functionalities such as retrieving games, searching for titles,
 * comparing game attributes, and validating user guesses.
 *
 * @author machm
 */
class ImageQuizControllerTest {

    private ImageQuizService imageQuizService;
    private ImageQuizController controller;

    /**
     * Sets up the test environment before each test execution.
     * <p>
     * Initializes the {@link ImageQuizService} and injects it into the {@link ImageQuizController}.
     * Note: This setup uses the real service implementation rather than a mock,
     * so it tests the integration between the controller and the service logic.
     */
    @BeforeEach
    void setUp() {
        imageQuizService = new ImageQuizService();
        controller = new ImageQuizController(imageQuizService);
    }

    /**
     * Verifies that the {@code getRandomGame} endpoint returns a valid response.
     * <p>
     * Expects a 200 OK status if a game is found, or 204 No Content if the game list is empty.
     */
    @Test
    void testGetRandomGame_ReturnsGame() {
        Game game = imageQuizService.getRandomGame();
        if (game == null) {
            game = new Game();
            game.setTitle("Test Game");
        }

        ResponseEntity<Game> response = controller.getRandomGame();
        if (response.getBody() != null) {
            assertEquals(200, response.getStatusCodeValue());
        } else {
            assertEquals(204, response.getStatusCodeValue());
        }
    }

    /**
     * Verifies that the {@code getAllGames} endpoint returns a list of all available games.
     * <p>
     * Asserts that the response status is 200 OK and the returned list is not null.
     */
    @Test
    void testGetAllGames_ReturnsList() {
        ResponseEntity<List<Game>> response = controller.getAllGames();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().size() >= 0);
    }

    /**
     * Verifies the search functionality.
     * <p>
     * Tests that searching for a specific string (e.g., "Game") returns a 200 OK status
     * and a non-null list of matching titles.
     */
    @Test
    void testSearchGames_ReturnsMatchingTitles() {
        ResponseEntity<List<String>> response = controller.searchGames("Game");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    /**
     * Tests the error handling of the {@code compare} endpoint.
     * <p>
     * Verifies that the server returns a 400 Bad Request status when required parameters
     * (in this case, the 'target' game) are missing from the request body.
     */
    @Test
    void testCompare_MissingParameters_ReturnsBadRequest() {
        Map<String, String> body = new HashMap<>();
        body.put("guess", "Game1");

        ResponseEntity<ComparisonResponse> response = controller.compare(body);
        assertEquals(400, response.getStatusCodeValue());
    }

}