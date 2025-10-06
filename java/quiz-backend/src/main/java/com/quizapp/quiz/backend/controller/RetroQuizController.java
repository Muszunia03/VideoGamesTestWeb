package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.RetroQuizService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retro-quiz")
@CrossOrigin(origins = "http://localhost:3000") // pozwala Reactowi na fetch
public class RetroQuizController {

    private final RetroQuizService retroQuizService;

    public RetroQuizController(RetroQuizService retroQuizService) {
        this.retroQuizService = retroQuizService;
    }

    // Zmiana na "/start", bo React robi fetch pod ten URL
    @GetMapping("/start")
    public List<Question> getRetroQuestions() {
        return retroQuizService.getRetroQuestions();
    }
}
