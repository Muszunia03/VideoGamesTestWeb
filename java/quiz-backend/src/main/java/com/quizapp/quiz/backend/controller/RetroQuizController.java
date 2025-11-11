package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.RetroQuizService;
import com.quizapp.quiz.backend.model.ResultRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retro-quiz")
@CrossOrigin(origins = "http://localhost:5173") // pozwala Reactowi się łączyć
public class RetroQuizController {

    private final RetroQuizService retroQuizService;

    public RetroQuizController(RetroQuizService retroQuizService) {
        this.retroQuizService = retroQuizService;
    }

    @GetMapping("/start")
    public List<Question> startQuiz() {
        return retroQuizService.getRetroQuestions();
    }

    @PostMapping("/save")
    public String saveResult(@RequestBody ResultRequest result) {
        retroQuizService.saveResult(
                result.getUserId(),
                result.getGameId(),
                result.getScore(),
                result.getTimeTakenSeconds()
        );
        return "Wynik został zapisany pomyślnie.";
    }
}
