package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.UserProfileDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link UserService} interacting with a real database.
 * <p>
 * <b>Note:</b> These tests require a running database instance and the full Spring context.
 * They verify that the service can correctly fetch real data (like users "Machu2003" or "TygrysGrazyna")
 * from the persistence layer.
 *
 * @author machm
 */
@SpringBootTest
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    /**
     * Verifies that the service retrieves the correct profile for an existing user.
     * <p>
     * Checks specific fields like email, creation date, and score to ensure data integrity.
     */
    @Test
    void testGetExistingUserProfile() {
        UserProfileDto profile = userService.getUserProfile("Machu2003");

        assertNotNull(profile);
        assertEquals("Machu2003", profile.getUsername());
        assertEquals("123mail123@mail.com", profile.getEmail());
        // Note: Exact timestamp matching in integration tests can be brittle if DB data changes
        assertEquals("2025-11-17 17:06:18.626519", profile.getCreatedAt());
        assertNotNull(profile.getTotalScore());
    }

    /**
     * Verifies that the service returns null when requesting a profile for a non-existent user.
     */
    @Test
    void testGetNonExistingUserProfile() {
        UserProfileDto profile = userService.getUserProfile("NieistniejacyUser");
        assertNull(profile);
    }

    /**
     * Verifies that the service retrieves the correct ID for an existing user.
     */
    @Test
    void testGetExistingUserId() {
        Integer id = userService.getUserIdByUsername("TygrysGrazyna");
        assertNotNull(id);
        assertEquals(19, id);
    }

    /**
     * Verifies that the service returns null when requesting an ID for a non-existent user.
     */
    @Test
    void testGetNonExistingUserId() {
        Integer id = userService.getUserIdByUsername("NieistniejacyUser");
        assertNull(id);
    }
}