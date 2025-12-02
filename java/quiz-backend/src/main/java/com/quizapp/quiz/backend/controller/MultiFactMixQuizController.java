package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.MultiFactMixQuizService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the "Multi-Fact Mix" quiz mode.
 * <p>
 * Handles requests for fetching questions that combine various facts about games
 * (e.g., release year, developer, genre) into a single challenge.
 *
 * @author machm
 */
@RestController
@RequestMapping("/api/multifactmix-quiz")
@CrossOrigin(origins = "http://localhost:5173")
public class MultiFactMixQuizController {

    private final MultiFactMixQuizService service;

    /**
     * Constructs the controller with the MultiFactMixQuizService dependency.
     *
     * @param service Service handling the logic for multi-fact questions.
     */
    public MultiFactMixQuizController(MultiFactMixQuizService service) {
        this.service = service;
    }

    /**
     * Fetches the next random question for the Multi-Fact Mix quiz.
     *
     * @return A {@link Question} object containing the question text, options, and correct answer.
     */
    @GetMapping("/next")
    public Question getNextQuestion() {
        return service.getNextQuestion();
    }
}