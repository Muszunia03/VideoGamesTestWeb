package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.MultiFactMixQuizService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/multifactmix-quiz")
@CrossOrigin(origins = "http://localhost:5173")
public class MultiFactMixQuizController {

    private final MultiFactMixQuizService service;

    public MultiFactMixQuizController(MultiFactMixQuizService service) {
        this.service = service;
    }

    @GetMapping("/next")
    public Question getNextQuestion() {
        return service.getNextQuestion();
    }
}
