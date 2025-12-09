package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.RatingEstimatorQuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link RatingEstimatorQuizController} class.
 * <p>
 * Validates the endpoint responsible for the "Rating Estimator" quiz mode.
 * Ensures that the controller correctly communicates with the service to retrieve questions.
 *
 * @author machm
 */
class RatingEstimatorQuizControllerTest {

    private RatingEstimatorQuizService service;
    private RatingEstimatorQuizController controller;

    /**
     * Sets up the test environment.
     * <p>
     * Creates a stub for {@link RatingEstimatorQuizService} that returns a fixed question
     * ("Guess the rating?" with correct answer "70") to ensure predictable test results.
     */
    @BeforeEach
    void setUp() {
        service = new RatingEstimatorQuizService() {
            @Override
            public Question getSingleRatingQuestion() {
                Question q = new Question();
                q.setQuestionText("Guess the rating?");
                q.setOptions(Arrays.asList("50", "60", "70", "80"));
                q.setCorrectAnswer("70");
                return q;
            }
        };
        controller = new RatingEstimatorQuizController(service);
    }

    /**
     * Verifies that {@code getNextQuestion} returns the expected question object.
     * <p>
     * Checks if the returned question is not null and matches the text, options count,
     * and correct answer defined in the service stub.
     */
    @Test
    void testGetNextQuestion() {
        Question question = controller.getNextQuestion();
        assertNotNull(question);
        assertEquals("Guess the rating?", question.getQuestionText());
        assertEquals(4, question.getOptions().size());
        assertEquals("70", question.getCorrectAnswer());
    }
}