package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.GenreChallengeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/genre-challenge")
public class GenreChallengeController {

    private final GenreChallengeService genreChallengeService;

    public GenreChallengeController(GenreChallengeService genreChallengeService) {
        this.genreChallengeService = genreChallengeService;
    }

    /**
     * Returns a single random question for the Genre Challenge quiz.
     */
    @GetMapping("/next")
    public Question getNextQuestion() {
        return genreChallengeService.getRandomQuestion();
    }
}
