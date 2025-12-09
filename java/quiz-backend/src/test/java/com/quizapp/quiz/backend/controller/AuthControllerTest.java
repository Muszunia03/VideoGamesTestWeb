package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.*;
import com.quizapp.quiz.backend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link AuthController} class.
 * <p>
 * These tests verify the logic of authentication endpoints (login and registration)
 * by using a stubbed implementation of {@link AuthService}. This ensures that the
 * controller logic is tested in isolation without relying on the actual database or external services.
 *
 * @author machm
 */
class AuthControllerTest {

    /**
     * The instance of the controller under test.
     */
    private AuthController controller;

    /**
     * Sets up the test environment before each test method execution.
     * <p>
     * Initializes the {@link AuthController} with an anonymous subclass (stub) of {@link AuthService}.
     * This stub simulates:
     * <ul>
     * <li>Successful login for user "user" with password "pass".</li>
     * <li>Registration failure if username is "takenUser".</li>
     * <li>Registration failure if email is "taken@example.com".</li>
     * <li>Success for all other registration attempts.</li>
     * </ul>
     */
    @BeforeEach
    void setUp() {
        AuthService authService = new AuthService() {
            @Override
            public boolean login(String login, String password) {
                return "user".equals(login) && "pass".equals(password);
            }

            @Override
            public String register(String username, String email, String password) {
                if ("takenUser".equals(username)) return "username_taken";
                if ("taken@example.com".equals(email)) return "email_taken";
                return "success";
            }
        };
        controller = new AuthController(authService);
    }

    /**
     * Verifies that the login endpoint returns a success response when valid credentials are provided.
     */
    @Test
    void testLogin_Success() {
        LoginRequest request = new LoginRequest();
        request.setLogin("user");
        request.setPassword("pass");

        AuthResponse response = controller.login(request);
        assertEquals("success", response.getStatus());
        assertEquals("Logged in successfully!", response.getMessage());
    }

    /**
     * Verifies that the login endpoint returns an error response when invalid credentials are provided.
     */
    @Test
    void testLogin_Failure() {
        LoginRequest request = new LoginRequest();
        request.setLogin("user");
        request.setPassword("wrong");

        AuthResponse response = controller.login(request);
        assertEquals("error", response.getStatus());
        assertEquals("Invalid username or password.", response.getMessage());
    }

    /**
     * Verifies that the register endpoint returns a success response when unique user details are provided.
     */
    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newUser");
        request.setEmail("new@example.com");
        request.setPassword("pass");

        AuthResponse response = controller.register(request);
        assertEquals("success", response.getStatus());
        assertEquals("Registered successfully!", response.getMessage());
    }

    /**
     * Verifies that the register endpoint returns an error response when the username is already taken.
     */
    @Test
    void testRegister_UsernameTaken() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("takenUser");
        request.setEmail("new@example.com");
        request.setPassword("pass");

        AuthResponse response = controller.register(request);
        assertEquals("error", response.getStatus());
        assertEquals("Username already taken.", response.getMessage());
    }

    /**
     * Verifies that the register endpoint returns an error response when the email is already registered.
     */
    @Test
    void testRegister_EmailTaken() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newUser");
        request.setEmail("taken@example.com");
        request.setPassword("pass");

        AuthResponse response = controller.register(request);
        assertEquals("error", response.getStatus());
        assertEquals("Email already registered.", response.getMessage());
    }
}