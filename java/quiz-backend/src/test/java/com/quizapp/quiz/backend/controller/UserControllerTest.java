package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.UserProfileDto;
import com.quizapp.quiz.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link UserController} class.
 * <p>
 * This class validates user-related endpoints, including retrieving user IDs by username
 * and fetching user profiles. It uses a stubbed {@link UserService} to simulate
 * backend data retrieval.
 *
 * @author machm
 */
class UserControllerTest {

    private UserService userService;
    private UserController controller;

    /**
     * Sets up the test environment.
     * <p>
     * Creates a {@link UserService} stub that:
     * <ul>
     * <li>Returns ID 1 for "Alice" and ID 2 for "Bob".</li>
     * <li>Returns a full profile DTO for "Alice" and "Bob" with specific dummy data.</li>
     * <li>Returns {@code null} for unknown users.</li>
     * </ul>
     */
    @BeforeEach
    void setUp() {
        // Simple implementation of UserService for testing purposes
        userService = new UserService(null) {
            @Override
            public Integer getUserIdByUsername(String username) {
                if ("Alice".equalsIgnoreCase(username)) return 1;
                if ("Bob".equalsIgnoreCase(username)) return 2;
                return null;
            }

            @Override
            public UserProfileDto getUserProfile(String username) {
                if ("Alice".equalsIgnoreCase(username)) {
                    return new UserProfileDto("Alice", "alice@example.com", "2025-01-01", 150, false);
                }
                if ("Bob".equalsIgnoreCase(username)) {
                    return new UserProfileDto("Bob", "bob@example.com", "2025-02-01", 80, false);
                }
                return null;
            }
        };

        controller = new UserController(userService);
    }

    /**
     * Verifies that {@code getUserIdByUsername} returns the correct ID for known users.
     */
    @Test
    void testGetUserIdByUsername_ExistingUser() {
        int id = controller.getUserIdByUsername("Alice");
        assertEquals(1, id);

        int id2 = controller.getUserIdByUsername("Bob");
        assertEquals(2, id2);
    }

    /**
     * Verifies that {@code getUserProfile} returns a correctly populated DTO for known users.
     */
    @Test
    void testGetUserProfile_ExistingUser() {
        UserProfileDto profile = controller.getUserProfile("Alice");
        assertNotNull(profile);
        assertEquals("Alice", profile.getUsername());
        assertEquals("alice@example.com", profile.getEmail());
        assertEquals("2025-01-01", profile.getCreatedAt());
        assertEquals(150, profile.getTotalScore());

        UserProfileDto profile2 = controller.getUserProfile("Bob");
        assertNotNull(profile2);
        assertEquals("Bob", profile2.getUsername());
        assertEquals(80, profile2.getTotalScore());
    }

    /**
     * Verifies that {@code getUserProfile} returns {@code null} when requesting a profile for a non-existent user.
     */
    @Test
    void testGetUserProfile_NonExistingUser() {
        UserProfileDto profile = controller.getUserProfile("Charlie");
        assertNull(profile);
    }
}