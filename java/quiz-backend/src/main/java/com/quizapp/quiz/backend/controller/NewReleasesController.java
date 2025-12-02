package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.NewReleasesService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the "New Releases" quiz mode.
 * <p>
 * Focuses on questions related to recently released video games.
 * Accessible via /api/new-releases.
 *
 * @author machm
 */
@RestController
@RequestMapping("/api/new-releases")
@CrossOrigin(origins = "http://localhost:5173") // React frontend
public class NewReleasesController {

    private final NewReleasesService newReleasesService;

    /**
     * Constructs the controller with the NewReleasesService dependency.
     *
     * @param newReleasesService Service handling questions about new game releases.
     */
    public NewReleasesController(NewReleasesService newReleasesService) {
        this.newReleasesService = newReleasesService;
    }

    /**
     * Starts the quiz session by providing the first question.
     * <p>
     * This endpoint is optionally distinct from /next to allow specific initialization logic if needed.
     *
     * @return A {@link Question} object to begin the quiz.
     */
    @GetMapping("/start")
    public Question startQuiz() {
        return newReleasesService.getRandomQuestion();
    }

    /**
     * Retrieves the next question in the quiz flow.
     *
     * @return A {@link Question} object containing the next challenge.
     */
    @GetMapping("/next")
    public Question nextQuestion() {
        return newReleasesService.getRandomQuestion();
    }
}