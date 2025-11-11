package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.model.ResultRequest;
import com.quizapp.quiz.backend.service.RatingEstimatorQuizService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rating-estimator")
@CrossOrigin(origins = "http://localhost:5173")
public class RatingEstimatorQuizController {

    private final RatingEstimatorQuizService ratingEstimatorQuizService;

    public RatingEstimatorQuizController(RatingEstimatorQuizService ratingEstimatorQuizService) {
        this.ratingEstimatorQuizService = ratingEstimatorQuizService;
    }

    @GetMapping("/start")
    public List<Question> startQuiz() {
        return ratingEstimatorQuizService.getRatingEstimatorQuestions();
    }

    @PostMapping("/save")
    public String saveResult(@RequestBody ResultRequest result) {
        ratingEstimatorQuizService.saveResult(
                result.getUserId(),
                result.getGameId(),
                result.getScore(),
                result.getTimeTakenSeconds()
        );
        return "Wynik zapisany pomyślnie.";
    }
}
