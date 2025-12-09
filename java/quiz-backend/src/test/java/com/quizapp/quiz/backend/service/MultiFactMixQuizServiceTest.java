package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link MultiFactMixQuizService} class.
 * <p>
 * Validates the generation of questions for the "Multi Fact Mix" category.
 * Ensures that the service produces well-formed questions with valid metadata and options.
 *
 * @author machm
 */
class MultiFactMixQuizServiceTest {

    private final MultiFactMixQuizService service = new MultiFactMixQuizService();

    /**
     * Verifies that {@code getNextQuestion} returns a valid, non-null {@link Question} object.
     * <p>
     * Checks:
     * <ul>
     * <li>The returned object is not null.</li>
     * <li>Crucial fields like title are present.</li>
     * <li>The template type falls within the valid range (0-6).</li>
     * <li>Internal consistency (question ID matching game ID).</li>
     * </ul>
     */
    @Test
    void testGetNextQuestionNotNull() {
        Question q = service.getNextQuestion();
        assertNotNull(q, "getNextQuestion should return a Question object");
        assertNotNull(q.getTitle(), "Question title should not be null");
        assertTrue(q.getTemplateType() >= 0 && q.getTemplateType() <= 6, "Template type should be between 0 and 6");
        assertEquals(q.getId(), q.getGameId(), "Question id and gameId should be equal");
    }

    /**
     * Verifies the integrity of the options and correct answer in the generated question.
     * <p>
     * Checks that the correct answer is either included in the list of options
     * (for multiple-choice) or is non-empty (for text input questions).
     */
    @Test
    void testGetNextQuestionOptionsAndAnswer() {
        Question q = service.getNextQuestion();
        assertNotNull(q.getOptions(), "Options list should not be null");
        assertNotNull(q.getCorrectAnswer(), "Correct answer should not be null");
        assertTrue(q.getOptions().isEmpty() || q.getOptions().contains(q.getCorrectAnswer())
                        || !q.getCorrectAnswer().isEmpty(),
                "Correct answer should be in options or non-empty for input type questions");
    }
}