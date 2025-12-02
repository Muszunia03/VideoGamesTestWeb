package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.RetroQuizService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the "Retro Quiz" game mode.
 * <p>
 * Handles requests related to fetching questions about classic and retro video games.
 * Accessible via /api/retro-quiz.
 *
 * @author machm
 */
@RestController
@RequestMapping("/api/retro-quiz")
@CrossOrigin(origins = "http://localhost:5173") // allow React to connect
public class RetroQuizController {

    private final RetroQuizService retroQuizService;

    /**
     * Constructs the controller with the RetroQuizService dependency.
     *
     * @param retroQuizService Service handling retro gaming questions logic.
     */
    public RetroQuizController(RetroQuizService retroQuizService) {
        this.retroQuizService = retroQuizService;
    }

    /**
     * Retrieves the next random retro-themed question.
     *
     * @return A {@link Question} object containing a retro game challenge.
     */
    @GetMapping("/next")
    public Question getNextQuestion() {
        return retroQuizService.getNextRetroQuestion();
    }
}