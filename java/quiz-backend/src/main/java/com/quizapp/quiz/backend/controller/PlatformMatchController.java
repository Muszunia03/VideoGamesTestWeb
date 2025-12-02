package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.PlatformMatchService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the "Platform Match" quiz mode.
 * <p>
 * Handles requests for questions where the user must match a video game to its release platform.
 *
 * @author machm
 */
@RestController
@RequestMapping("/api/platform-match")
@CrossOrigin(origins = "http://localhost:5173")
public class PlatformMatchController {

    private final PlatformMatchService platformMatchService;

    /**
     * Constructs the controller with the PlatformMatchService dependency.
     *
     * @param platformMatchService Service logic for retrieving platform-related questions.
     */
    public PlatformMatchController(PlatformMatchService platformMatchService) {
        this.platformMatchService = platformMatchService;
    }

    /**
     * Retrieves the next question for the Platform Match quiz.
     *
     * @return A {@link Question} object asking the user to identify the correct platform.
     */
    @GetMapping("/next")
    public Question nextQuestion() {
        return platformMatchService.getSinglePlatformQuestion();
    }
}