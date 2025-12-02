package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.GenreChallengeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the "Genre Challenge" quiz mode.
 * <p>
 * Handles requests related to fetching questions categorized by game genres.
 *
 * @author machm
 */
@RestController
@RequestMapping("/api/genre-challenge")
public class GenreChallengeController {

    private final GenreChallengeService genreChallengeService;

    /**
     * Constructs the controller with the GenreChallengeService dependency.
     *
     * @param genreChallengeService Service for retrieving genre-based questions.
     */
    public GenreChallengeController(GenreChallengeService genreChallengeService) {
        this.genreChallengeService = genreChallengeService;
    }

    /**
     * Retrieves a single random question for the Genre Challenge.
     * <p>
     * Used to fetch the next question in the quiz flow.
     *
     * @return A {@link Question} object containing the question text, options, and answer.
     */
    @GetMapping("/next")
    public Question getNextQuestion() {
        return genreChallengeService.getRandomQuestion();
    }
}