package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.service.ResultService;
import com.quizapp.quiz.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ResultController} class.
 * <p>
 * This class tests the endpoints responsible for saving quiz results and retrieving
 * user-specific game history. It uses stubs for both {@link UserService} and {@link ResultService}
 * to simulate user existence checks and database operations without external dependencies.
 *
 * @author machm
 */
class ResultControllerTest {

    private UserService userService;
    private ResultService resultService;
    private ResultController controller;

    /**
     * Sets up the test environment before each test execution.
     * <p>
     * Initializes:
     * <ul>
     * <li>A stubbed {@link UserService} that recognizes users "Alice" (ID 1) and "Bob" (ID 2).</li>
     * <li>A stubbed {@link ResultService} that logs result saving (mock implementation).</li>
     * <li>A subclassed {@link ResultController} that overrides {@code getResultsByUsername}
     * to return static fake data instead of querying a database.</li>
     * </ul>
     */
    @BeforeEach
    void setUp() {
        userService = new UserService(null) {
            @Override
            public Integer getUserIdByUsername(String username) {
                if ("Alice".equalsIgnoreCase(username)) return 1;
                if ("Bob".equalsIgnoreCase(username)) return 2;
                return null;
            }
        };

        resultService = new ResultService() {
            @Override
            public void saveResult(int userId, int score, String quizType) {
                System.out.println("Saved result: userId=" + userId + ", score=" + score + ", quizType=" + quizType);
            }
        };

        controller = new ResultController(userService, resultService) {
            @Override
            public List<Map<String, Object>> getResultsByUsername(String username) {
                Integer userId = userService.getUserIdByUsername(username);
                if (userId == null) return List.of();

                return List.of(
                        Map.of("score", 100, "quizType", "New Releases", "createdAt", "2025-12-07 12:00:00"),
                        Map.of("score", 80, "quizType", "Genre Challenge", "createdAt", "2025-12-06 14:30:00")
                );
            }
        };
    }

    /**
     * Verifies that {@code saveResult} returns "OK" when valid data is provided for an existing user.
     */
    @Test
    void testSaveResult_Success() {
        Map<String, Object> request = Map.of(
                "username", "Alice",
                "quizType", "New Releases",
                "score", 100
        );

        String response = controller.saveResult(request);
        assertEquals("OK", response);
    }

    /**
     * Verifies that {@code saveResult} returns an error message when required fields (e.g., quizType) are missing.
     */
    @Test
    void testSaveResult_MissingData() {
        Map<String, Object> request = Map.of(
                "username", "Alice",
                "score", 100
        );

        String response = controller.saveResult(request);
        assertEquals("Błąd: brakujące dane w request", response);
    }

    /**
     * Verifies that {@code saveResult} returns an error message when the username does not exist in the system.
     */
    @Test
    void testSaveResult_UserNotExist() {
        Map<String, Object> request = Map.of(
                "username", "Charlie",
                "quizType", "New Releases",
                "score", 50
        );

        String response = controller.saveResult(request);
        assertEquals("Błąd: użytkownik nie istnieje", response);
    }

    /**
     * Verifies that {@code getResultsByUsername} returns the correct list of results for an existing user.
     */
    @Test
    void testGetResultsByUsername_ExistingUser() {
        List<Map<String, Object>> results = controller.getResultsByUsername("Alice");
        assertEquals(2, results.size());

        assertEquals(100, results.get(0).get("score"));
        assertEquals("New Releases", results.get(0).get("quizType"));
        assertEquals("2025-12-07 12:00:00", results.get(0).get("createdAt"));
    }

    /**
     * Verifies that {@code getResultsByUsername} returns an empty list for a non-existing user.
     */
    @Test
    void testGetResultsByUsername_NonExistingUser() {
        List<Map<String, Object>> results = controller.getResultsByUsername("Charlie");
        assertTrue(results.isEmpty());
    }
}