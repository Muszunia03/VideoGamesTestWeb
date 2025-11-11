package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.SpeedrunQuizService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/speedrun")
@CrossOrigin(origins = "http://localhost:3000")
public class SpeedrunQuizController {

    private final SpeedrunQuizService speedrunQuizService;

    public SpeedrunQuizController(SpeedrunQuizService speedrunQuizService) {
        this.speedrunQuizService = speedrunQuizService;
    }

    @GetMapping("/start")
    public List<Question> startQuiz(@RequestParam(name = "count", defaultValue = "10") int count) {
        return speedrunQuizService.generateSpeedrunQuiz(count);
    }

}
