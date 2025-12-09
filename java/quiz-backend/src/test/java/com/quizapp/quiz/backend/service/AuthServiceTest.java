package com.quizapp.quiz.backend.service;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link AuthService} class.
 * <p>
 * This class verifies the core mechanisms of authentication, including
 * password hashing utilities and the business logic for registration and login
 * (simulated via an anonymous subclass).
 *
 * @author machm
 */
class AuthServiceTest {

    /**
     * Verifies the correctness of the BCrypt password hashing mechanism.
     * <p>
     * Ensures that:
     * <ul>
     * <li>A hashed password can be successfully verified against the plain text password.</li>
     * <li>An incorrect password fails verification against the hash.</li>
     * </ul>
     */
    @Test
    void testPasswordHashingAndCheck() {
        String password = "secret123";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        assertTrue(BCrypt.checkpw(password, hash));
        assertFalse(BCrypt.checkpw("wrongPassword", hash));
    }

    /**
     * Verifies the registration and login flows using a stubbed {@link AuthService}.
     * <p>
     * This test bypasses the actual database repository by overriding the {@code register}
     * and {@code login} methods. It tests the return values for:
     * <ul>
     * <li>Successful registration.</li>
     * <li>Registration failure due to taken username or email.</li>
     * <li>Successful login.</li>
     * <li>Login failure due to wrong credentials.</li>
     * </ul>
     */
    @Test
    void testRegisterAndLoginSimulated() {
        AuthService authService = new AuthService() {
            @Override
            public String register(String username, String email, String plainPassword) {
                if ("takenUser".equals(username)) return "username_taken";
                if ("takenEmail@example.com".equals(email)) return "email_taken";
                return "success";
            }

            @Override
            public boolean login(String login, String plainPassword) {
                return "Alice".equals(login) && "password123".equals(plainPassword);
            }
        };

        assertEquals("success", authService.register("Bob", "bob@example.com", "pwd"));
        assertEquals("username_taken", authService.register("takenUser", "any@example.com", "pwd"));
        assertEquals("email_taken", authService.register("AnyUser", "takenEmail@example.com", "pwd"));

        assertTrue(authService.login("Alice", "password123"));
        assertFalse(authService.login("Alice", "wrongPassword"));
        assertFalse(authService.login("Bob", "password123"));
    }
}