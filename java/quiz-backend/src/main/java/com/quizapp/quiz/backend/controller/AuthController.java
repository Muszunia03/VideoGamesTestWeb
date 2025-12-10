package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.*;
import com.quizapp.quiz.backend.service.AuthService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling user authentication and registration processes.
 * <p>
 * Provides endpoints for logging in existing users and registering new accounts.
 * Accessible via /api/auth.
 *
 * @author machm
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructs the AuthController with the necessary dependency.
     *
     * @param authService Service handling authentication business logic.
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Authenticates a user based on the provided credentials.
     *
     * @param request The {@link LoginRequest} object containing the login (username) and password.
     * @return An {@link AuthResponse} containing the status ("success" or "error") and a descriptive message.
     */
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        boolean success = authService.login(request.getLogin(), request.getPassword());
        if (success) {
            return new AuthResponse("success", "Logged in successfully!");
        } else {
            return new AuthResponse("error", "Invalid username, password or account is banned!.");
        }
    }

    /**
     * Registers a new user in the system.
     *
     * @param request The {@link RegisterRequest} object containing username, email, and password.
     * @return An {@link AuthResponse} indicating the result of the registration attempt:
     * <ul>
     * <li>success: User created successfully.</li>
     * <li>error: Username taken, email taken, or generic failure.</li>
     * </ul>
     */
    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        String result = authService.register(request.getUsername(), request.getEmail(), request.getPassword());
        return switch (result) {
            case "success" -> new AuthResponse("success", "Registered successfully!");
            case "username_taken" -> new AuthResponse("error", "Username already taken.");
            case "email_taken" -> new AuthResponse("error", "Email already registered.");
            default -> new AuthResponse("error", "Registration failed.");
        };
    }
}