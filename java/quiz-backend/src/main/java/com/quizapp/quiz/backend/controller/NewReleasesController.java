package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.NewReleasesService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/new-releases")
@CrossOrigin(origins = "http://localhost:5173") // React frontend
public class NewReleasesController {

    private final NewReleasesService newReleasesService;

    public NewReleasesController(NewReleasesService newReleasesService) {
        this.newReleasesService = newReleasesService;
    }

    // Start endpoint (optional, could load first question)
    @GetMapping("/start")
    public Question startQuiz() {
        return newReleasesService.getRandomQuestion();
    }

    // Next question endpoint
    @GetMapping("/next")
    public Question nextQuestion() {
        return newReleasesService.getRandomQuestion();
    }
}
