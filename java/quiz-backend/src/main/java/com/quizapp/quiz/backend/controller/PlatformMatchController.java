package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.PlatformMatchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/platform-match")
@CrossOrigin(origins = "http://localhost:5173")
public class PlatformMatchController {

    private final PlatformMatchService platformMatchService;

    public PlatformMatchController(PlatformMatchService platformMatchService) {
        this.platformMatchService = platformMatchService;
    }

    @GetMapping("/next")
    public Question nextQuestion() {
        return platformMatchService.getSinglePlatformQuestion();
    }
}
