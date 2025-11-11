package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.model.ResultRequest;
import com.quizapp.quiz.backend.service.GenreChallengeService;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/genre-challenge")
public class GenreChallengeController {

    private final GenreChallengeService genreChallengeService;

    public GenreChallengeController(GenreChallengeService genreChallengeService) {
        this.genreChallengeService = genreChallengeService;
    }

    /**
     * Endpoint to start the Genre Challenge quiz.
     * GET /api/genre-challenge/start?limit=5
     */
    @GetMapping("/start")
    public List<Question> startQuiz(@RequestParam(name = "limit", defaultValue = "10") int limit) {
        try {
            return genreChallengeService.generateQuestions(limit);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate quiz questions.");
        }
    }

    /**
     * Endpoint to save the result of the Genre Challenge quiz.
     * POST /api/genre-challenge/save-result
     */
    @PostMapping("/save-result")
    public void saveResult(@RequestBody ResultRequest resultRequest) {
        genreChallengeService.saveResult(
                resultRequest.getUserId(),
                resultRequest.getGameId(),
                resultRequest.getScore(),
                resultRequest.getTimeTakenSeconds()
        );
    }
}
