package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.RetroQuizService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/retro-quiz")
@CrossOrigin(origins = "http://localhost:5173") // allow React to connect
public class RetroQuizController {

    private final RetroQuizService retroQuizService;

    public RetroQuizController(RetroQuizService retroQuizService) {
        this.retroQuizService = retroQuizService;
    }

    @GetMapping("/next")
    public Question getNextQuestion() {
        return retroQuizService.getNextRetroQuestion();
    }
}
