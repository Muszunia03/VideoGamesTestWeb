package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.resources.LeaderboardEntry;
import com.quizapp.quiz.backend.service.ResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link LeaderboardController} class.
 * <p>
 * This class verifies the functionality of the leaderboard retrieval endpoint.
 * It uses a stubbed version of {@link ResultService} to simulate different
 * states of the data source (e.g., populated list vs. empty list).
 *
 * @author machm
 */
class LeaderboardControllerTest {

    private ResultService resultService;
    private LeaderboardController controller;

    /**
     * Sets up the test environment before each test execution.
     * <p>
     * Initializes the {@link LeaderboardController} with a default stub of {@link ResultService}.
     * By default, this stub provides a list containing two sample entries ("Alice" and "Bob").
     * This ensures a known state for tests that expect data, although specific tests
     * may override this service behavior.
     */
    @BeforeEach
    void setUp() {
        resultService = new ResultService() {
            @Override
            public List<LeaderboardEntry> getLeaderboard() {
                return List.of(
                        new LeaderboardEntry("Alice", 100, "Latest Releases"),
                        new LeaderboardEntry("Bob", 80, "Genre Challenge")
                );
            }
        };

        controller = new LeaderboardController(resultService);
    }

    /**
     * Verifies that the {@code getLeaderboard} endpoint returns an empty list
     * when the service provides no data.
     * <p>
     * This test overrides the default service stub defined in {@code setUp()}
     * with one that returns an empty list, asserting that the controller
     * correctly propagates this empty state.
     */
    @Test
    void testGetLeaderboard_EmptyList() {
        // Override the service to return an empty list for this specific test case
        resultService = new ResultService() {
            @Override
            public List<LeaderboardEntry> getLeaderboard() {
                return List.of();
            }
        };
        controller = new LeaderboardController(resultService);

        List<LeaderboardEntry> response = controller.getLeaderboard();
        assertTrue(response.isEmpty(), "Leaderboard should be empty when service returns no entries.");
    }
}