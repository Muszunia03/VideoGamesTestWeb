package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.model.ResultRequest;
import com.quizapp.quiz.backend.service.MultiFactMixQuizService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/multifactmix-quiz")
@CrossOrigin(origins = "http://localhost:5173")
public class MultiFactMixQuizController {

    private final MultiFactMixQuizService multiFactMixQuizService;

    public MultiFactMixQuizController(MultiFactMixQuizService multiFactMixQuizService) {
        this.multiFactMixQuizService = multiFactMixQuizService;
    }

    @GetMapping("/start")
    public List<Question> startQuiz() {
        return multiFactMixQuizService.getMultiFactMixQuestions();
    }

    @PostMapping("/save")
    public String saveResult(@RequestBody ResultRequest result) {
        multiFactMixQuizService.saveResult(
                result.getUserId(),
                result.getGameId(),
                result.getScore(),
                result.getTimeTakenSeconds()
        );
        return "Wynik zapisany pomyślnie.";
    }
}
