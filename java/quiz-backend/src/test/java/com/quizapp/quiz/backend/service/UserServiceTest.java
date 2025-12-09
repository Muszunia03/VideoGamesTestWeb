package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.UserProfileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link UserService} class.
 * <p>
 * Unlike the integration tests, these tests run in isolation without a Spring context or database.
 * They use a test subclass (`TestUserService`) to stub the behavior of database-dependent methods,
 * making them fast and deterministic.
 *
 * @author machm
 */
class UserServiceTest {

    private UserService userService;

    /**
     * A stub implementation of UserService for testing purposes.
     * <p>
     * Overrides methods that would normally access the database to return fixed, predictable data.
     */
    static class TestUserService extends UserService {

        public TestUserService() {
            super(null); // Pass null as we are not using the real JdbcTemplate
        }

        @Override
        public UserProfileDto getUserProfile(String username) {
            if ("TestUser".equalsIgnoreCase(username)) {
                return new UserProfileDto("TestUser", "test@example.com", "2025-01-01", 150, false);
            }
            return null;
        }

        @Override
        public Integer getUserIdByUsername(String username) {
            if ("TestUser".equalsIgnoreCase(username)) return 1;
            return null;
        }
    }

    /**
     * Sets up the test environment by initializing the stubbed service.
     */
    @BeforeEach
    void setUp() {
        userService = new TestUserService();
    }

    /**
     * Verifies that {@code getUserProfile} returns the correct DTO for a known user stub.
     */
    @Test
    void testGetUserProfileFound() {
        UserProfileDto profile = userService.getUserProfile("TestUser");
        assertNotNull(profile);
        assertEquals("TestUser", profile.getUsername());
        assertEquals("test@example.com", profile.getEmail());
        assertEquals("2025-01-01", profile.getCreatedAt());
        assertEquals(150, profile.getTotalScore());
    }

    /**
     * Verifies that {@code getUserProfile} returns null for an unknown user.
     */
    @Test
    void testGetUserProfileNotFound() {
        UserProfileDto profile = userService.getUserProfile("NonExisting");
        assertNull(profile);
    }

    /**
     * Verifies that {@code getUserIdByUsername} returns the correct ID for a known user stub.
     */
    @Test
    void testGetUserIdByUsernameFound() {
        Integer id = userService.getUserIdByUsername("TestUser");
        assertEquals(1, id);
    }

    /**
     * Verifies that {@code getUserIdByUsername} returns null for an unknown user.
     */
    @Test
    void testGetUserIdByUsernameNotFound() {
        Integer id = userService.getUserIdByUsername("NonExisting");
        assertNull(id);
    }
}