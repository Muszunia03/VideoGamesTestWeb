package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link RatingEstimatorQuizService} class.
 * <p>
 * Validates the generation of rating-based questions.
 * Ensures that the service produces valid questions with appropriate templates and options.
 *
 * @author machm
 */
class RatingEstimatorQuizServiceTest {

    private final RatingEstimatorQuizService service = new RatingEstimatorQuizService();

    /**
     * Verifies that {@code getSingleRatingQuestion} returns a well-formed {@link Question} object.
     * <p>
     * Checks for the presence of:
     * <ul>
     * <li>Question text and title.</li>
     * <li>A valid correct answer.</li>
     * <li>A non-null options list.</li>
     * <li>A valid template ID (0-4).</li>
     * </ul>
     */
    @Test
    void testGetSingleRatingQuestionNotNull() {
        Question q = service.getSingleRatingQuestion();
        assertNotNull(q, "getSingleRatingQuestion should return a Question object");
        assertNotNull(q.getTitle(), "Question title should not be null");
        assertNotNull(q.getQuestionText(), "Question text should not be null");
        assertNotNull(q.getCorrectAnswer(), "Correct answer should not be null");
        assertNotNull(q.getOptions(), "Options list should not be null");
        assertTrue(q.getTemplateType() >= 0 && q.getTemplateType() <= 4, "Template type should be between 0 and 4");
    }

    /**
     * Verifies logic consistency regarding the answer and options.
     * <p>
     * Ensures that the correct answer is contained within the generated options list,
     * unless the list is empty (which implies a direct input question).
     */
    @Test
    void testCorrectAnswerInOptionsOrInput() {
        Question q = service.getSingleRatingQuestion();
        assertNotNull(q);
        assertTrue(q.getOptions().isEmpty() || q.getOptions().contains(q.getCorrectAnswer()),
                "Correct answer should be in options or input question (empty options)");
    }
}