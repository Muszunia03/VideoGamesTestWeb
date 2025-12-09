package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.resources.LeaderboardEntry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ResultService} class.
 * <p>
 * This class validates the business logic for saving results and retrieving the leaderboard.
 * Note: These tests assume a default or stubbed behavior of the underlying database access
 * (depending on the service implementation), or they test logic that doesn't strictly depend on DB state.
 *
 * @author machm
 */
class ResultServiceTest {

    private final ResultService service = new ResultService();

    /**
     * Verifies that saving a result does not throw any exceptions.
     * <p>
     * Tests the "happy path" for saving a user's score.
     */
    @Test
    void testSaveResult() {
        assertDoesNotThrow(() -> service.saveResult(1, 100, "MultiFactMix"));
    }

    /**
     * Verifies the structure of the returned leaderboard.
     * <p>
     * Checks that:
     * <ul>
     * <li>The returned list is not null.</li>
     * <li>Each entry contains a valid username and quiz type.</li>
     * <li>The score is non-negative.</li>
     * </ul>
     */
    @Test
    void testGetLeaderboardNotNull() {
        List<LeaderboardEntry> leaderboard = service.getLeaderboard();
        assertNotNull(leaderboard, "Leaderboard should not be null");
        for (LeaderboardEntry entry : leaderboard) {
            assertNotNull(entry.getUsername(), "Username should not be null");
            assertTrue(entry.getScore() >= 0, "Score should be non-negative");
            assertNotNull(entry.getQuizType(), "Quiz type should not be null");
        }
    }

    /**
     * Verifies that the leaderboard size is limited.
     * <p>
     * Ensures the service returns at most 20 entries, preventing data overload on the frontend.
     */
    @Test
    void testLeaderboardLimit() {
        List<LeaderboardEntry> leaderboard = service.getLeaderboard();
        assertTrue(leaderboard.size() <= 20, "Leaderboard should return at most 20 entries");
    }
}