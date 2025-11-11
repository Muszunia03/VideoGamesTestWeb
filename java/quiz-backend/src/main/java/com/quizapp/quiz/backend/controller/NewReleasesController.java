package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.model.ResultRequest;
import com.quizapp.quiz.backend.service.NewReleasesService;
import com.quizapp.quiz.backend.service.RetroQuizService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/new-releases")
public class NewReleasesController {

    private final NewReleasesService newReleasesService;
    private final RetroQuizService retroQuizService;

    public NewReleasesController(NewReleasesService newReleasesService, RetroQuizService retroQuizService) {
        this.newReleasesService = newReleasesService;
        this.retroQuizService = retroQuizService;
    }

    @GetMapping("/start")
    public List<Question> startQuiz() {
        return newReleasesService.getNewReleaseQuestions();
    }

    @PostMapping("/save-result")
    public void saveResult(@RequestBody ResultRequest resultRequest) {
        retroQuizService.saveResult(
                resultRequest.getUserId(),
                resultRequest.getGameId(),
                resultRequest.getScore(),
                resultRequest.getTimeTakenSeconds()
        );
    }
}
