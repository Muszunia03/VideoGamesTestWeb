package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.UserProfileDto;
import com.quizapp.quiz.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/id/{username}")
    public int getUserIdByUsername(@PathVariable String username) {
        return userService.getUserIdByUsername(username);
    }

    @GetMapping("/profile/{username}")
    public UserProfileDto getUserProfile(@PathVariable String username) {
        return userService.getUserProfile(username);
    }
}
