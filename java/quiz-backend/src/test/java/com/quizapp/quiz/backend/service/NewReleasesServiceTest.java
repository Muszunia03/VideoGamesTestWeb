package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link NewReleasesService} class.
 * <p>
 * Validates the functionality of the "New Releases" quiz generation.
 * Ensures that questions related to recent games are created with correct structure and data.
 *
 * @author machm
 */
class NewReleasesServiceTest {

    private final NewReleasesService service = new NewReleasesService();

    /**
     * Verifies that {@code getRandomQuestion} returns a fully populated {@link Question} object.
     * <p>
     * Checks for the presence of:
     * <ul>
     * <li>Question text and title.</li>
     * <li>Correct answer field.</li>
     * <li>Options list (should not be null).</li>
     * <li>Valid template type (0-3).</li>
     * </ul>
     */
    @Test
    void testGetRandomQuestionNotNull() {
        Question q = service.getRandomQuestion();
        assertNotNull(q, "getRandomQuestion should return a Question object");
        assertNotNull(q.getTitle(), "Question title should not be null");
        assertNotNull(q.getQuestionText(), "Question text should not be null");
        assertNotNull(q.getCorrectAnswer(), "Correct answer should not be null");
        assertNotNull(q.getOptions(), "Options list should not be null");
        assertTrue(q.getTemplateType() >= 0 && q.getTemplateType() <= 3, "Template type should be between 0 and 3");
    }

    /**
     * Verifies logic consistency regarding the answer and options.
     * <p>
     * Ensures the correct answer is logically valid (either present in the options or a valid string).
     */
    @Test
    void testCorrectAnswerInOptionsOrNonEmpty() {
        Question q = service.getRandomQuestion();
        assertNotNull(q);
        assertTrue(q.getOptions().isEmpty()
                        || q.getOptions().contains(q.getCorrectAnswer())
                        || !q.getCorrectAnswer().isEmpty(),
                "Correct answer should be in options or non-empty for input type questions");
    }
}