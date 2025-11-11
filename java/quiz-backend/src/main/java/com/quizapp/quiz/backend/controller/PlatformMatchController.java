package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.model.ResultRequest;
import com.quizapp.quiz.backend.service.PlatformMatchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platform-match")
@CrossOrigin(origins = "http://localhost:5173")
public class PlatformMatchController {

    private final PlatformMatchService platformMatchService;

    public PlatformMatchController(PlatformMatchService platformMatchService) {
        this.platformMatchService = platformMatchService;
    }

    @GetMapping("/start")
    public List<Question> startQuiz() {
        return platformMatchService.getPlatformMatchQuestions();
    }

    @PostMapping("/save-result")
    public String saveResult(@RequestBody ResultRequest request) {
        platformMatchService.saveResult(
                request.getUserId(),
                request.getGameId(),
                request.getScore(),
                request.getTimeTakenSeconds()
        );
        return "Result saved successfully!";
    }
}
