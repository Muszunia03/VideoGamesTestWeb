package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link RetroQuizService} class.
 * <p>
 * Validates the generation of questions for the "Retro Quiz" category.
 * Ensures the service correctly handles different templates (e.g., debut platform, yes/no).
 *
 * @author machm
 */
class RetroQuizServiceTest {

    private final RetroQuizService service = new RetroQuizService();

    /**
     * Verifies that {@code getNextRetroQuestion} returns a valid {@link Question} object.
     * <p>
     * Checks for:
     * <ul>
     * <li>Non-null return value.</li>
     * <li>Presence of title, question text, and correct answer.</li>
     * <li>Valid template type range (0-3).</li>
     * </ul>
     */
    @Test
    void testGetNextRetroQuestionNotNull() {
        Question q = service.getNextRetroQuestion();
        assertNotNull(q, "Returned question should not be null");
        assertNotNull(q.getTitle(), "Question title should not be null");
        assertTrue(q.getTemplateType() >= 0 && q.getTemplateType() <= 3, "Template type should be 0–3");
        assertNotNull(q.getQuestionText(), "Question text should not be null");
        assertNotNull(q.getCorrectAnswer(), "Correct answer should not be null");
    }

    /**
     * Verifies the consistency of options based on the template type.
     * <p>
     * Checks logic such as:
     * <ul>
     * <li>Template 0 (Platform): Must have options.</li>
     * <li>Templates 1-3 (Yes/No): Must have exactly 2 options.</li>
     * </ul>
     */
    @Test
    void testOptionsConsistency() {
        Question q = service.getNextRetroQuestion();
        assertNotNull(q);
        switch (q.getTemplateType()) {
            case 0 -> assertTrue(q.getOptions().size() >= 1, "Debut platform should have at least one option");
            case 1, 2, 3 -> assertEquals(2, q.getOptions().size(), "Yes/No question should have 2 options");
        }
    }
}