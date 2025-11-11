package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.*;
import com.quizapp.quiz.backend.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // Twój React
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        boolean success = authService.login(request.getLogin(), request.getPassword());
        if (success) {
            return new AuthResponse("success", "Logged in successfully!");
        } else {
            return new AuthResponse("error", "Invalid username or password.");
        }
    }

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
