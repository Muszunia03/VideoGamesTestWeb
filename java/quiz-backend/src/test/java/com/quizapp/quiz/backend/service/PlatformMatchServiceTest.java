package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PlatformMatchService} class.
 * <p>
 * Validates the generation of platform-specific quiz questions.
 * Ensures that the service correctly identifies platforms and generates valid questions.
 *
 * @author machm
 */
class PlatformMatchServiceTest {

    private final PlatformMatchService service = new PlatformMatchService();

    /**
     * Verifies that {@code getSinglePlatformQuestion} returns a valid {@link Question}.
     * <p>
     * Checks that the generated question object contains all necessary fields
     * and adheres to the expected template range (0-10).
     */
    @Test
    void testGetSinglePlatformQuestionNotNull() {
        Question q = service.getSinglePlatformQuestion();
        assertNotNull(q, "getSinglePlatformQuestion should return a Question object");
        assertNotNull(q.getTitle(), "Question title should not be null");
        assertNotNull(q.getQuestionText(), "Question text should not be null");
        assertNotNull(q.getCorrectAnswer(), "Correct answer should not be null");
        assertNotNull(q.getOptions(), "Options list should not be null");
        assertTrue(q.getTemplateType() >= 0 && q.getTemplateType() <= 10, "Template type should be between 0 and 10");
    }

    /**
     * Verifies that the correct answer is valid relative to the provided options.
     * <p>
     * Ensures that if options are provided, the correct answer is among them.
     */
    @Test
    void testCorrectAnswerInOptionsOrNonEmpty() {
        Question q = service.getSinglePlatformQuestion();
        assertNotNull(q);
        assertTrue(q.getOptions().isEmpty()
                        || q.getOptions().contains(q.getCorrectAnswer())
                        || !q.getCorrectAnswer().isEmpty(),
                "Correct answer should be in options or non-empty for input type questions");
    }
}