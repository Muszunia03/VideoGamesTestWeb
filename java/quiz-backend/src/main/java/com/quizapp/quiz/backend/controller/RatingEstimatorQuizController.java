package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.RatingEstimatorQuizService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rating-estimator")
@CrossOrigin(origins = "http://localhost:5173")
public class RatingEstimatorQuizController {

    private final RatingEstimatorQuizService ratingEstimatorQuizService;

    public RatingEstimatorQuizController(RatingEstimatorQuizService ratingEstimatorQuizService) {
        this.ratingEstimatorQuizService = ratingEstimatorQuizService;
    }

    @GetMapping("/next")
    public Question getNextQuestion() {
        return ratingEstimatorQuizService.getSingleRatingQuestion();
    }
}

