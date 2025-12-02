package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.UserProfileDto;
import com.quizapp.quiz.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for user profile management and identification.
 * <p>
 * Provides endpoints to fetch user IDs and detailed profiles.
 * Accessible via /api/users.
 *
 * @author machm
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    /**
     * Constructs the controller with the UserService dependency.
     *
     * @param userService Service for handling user-related data operations.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves the unique ID of a user based on their username.
     *
     * @param username The username to look up.
     * @return The integer ID associated with the given username.
     */
    @GetMapping("/id/{username}")
    public int getUserIdByUsername(@PathVariable String username) {
        return userService.getUserIdByUsername(username);
    }

    /**
     * Fetches the full profile data for a specific user.
     * <p>
     * This typically includes stats, registration date, or other public info
     * encapsulated in a DTO.
     *
     * @param username The username of the profile to retrieve.
     * @return A {@link UserProfileDto} containing the user's profile details.
     */
    @GetMapping("/profile/{username}")
    public UserProfileDto getUserProfile(@PathVariable String username) {
        return userService.getUserProfile(username);
    }
}