package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.RatingEstimatorQuizService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the "Rating Estimator" quiz mode.
 * <p>
 * In this mode, users guess the rating (e.g., Metacritic score) of a specific game
 * or determine if it is higher/lower than a reference value.
 *
 * @author machm
 */
@RestController
@RequestMapping("/api/rating-estimator")
@CrossOrigin(origins = "http://localhost:5173")
public class RatingEstimatorQuizController {

    private final RatingEstimatorQuizService ratingEstimatorQuizService;

    /**
     * Constructs the controller with the RatingEstimatorQuizService dependency.
     *
     * @param ratingEstimatorQuizService Service handling rating estimation logic.
     */
    public RatingEstimatorQuizController(RatingEstimatorQuizService ratingEstimatorQuizService) {
        this.ratingEstimatorQuizService = ratingEstimatorQuizService;
    }

    /**
     * Fetches the next rating-based question.
     *
     * @return A {@link Question} object related to game ratings.
     */
    @GetMapping("/next")
    public Question getNextQuestion() {
        return ratingEstimatorQuizService.getSingleRatingQuestion();
    }
}