package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.UserProfileDto;
import com.quizapp.quiz.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    /**
     * Handles the HTTP POST request to ban a user.
     * <p>
     * This method delegates the operation to the user service to mark the specified user
     * as banned in the system and returns a confirmation message.
     *
     * @param username the username extracted from the URL path of the user to be banned.
     * @return a text message confirming that the user has been banned.
     */
    @PostMapping("/ban/{username}")
    public String banUser(@PathVariable String username) {
        userService.banUser(username);
        return "User " + username + " has been banned.";
    }

    /**
     * Handles the HTTP POST request to unban (reactivate) a user.
     * <p>
     * This method delegates the operation to the user service to remove the ban status
     * from the specified user and returns a confirmation message.
     *
     * @param username the username extracted from the URL path of the user to be unbanned.
     * @return a text message confirming that the user has been unbanned.
     */
    @PostMapping("/unban/{username}")
    public String unbanUser(@PathVariable String username) {
        userService.unbanUser(username);
        return "User " + username + " has been unbanned.";
    }

    /**
     * Handles the HTTP GET request to retrieve a list of all registered users.
     * <p>
     * This endpoint fetches detailed user profiles, including their statistics,
     * total scores, and ban status.
     *
     * @return a list of {@link UserProfileDto} objects representing all users in the system.
     */
    @GetMapping("/all")
    public List<UserProfileDto> getAllUsers() {
        return userService.getAllUsers();
    }
}