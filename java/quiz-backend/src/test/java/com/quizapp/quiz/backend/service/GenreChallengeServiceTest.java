package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link GenreChallengeService} class.
 * <p>
 * Validates the {@code createQuestionFromGame} method, checking different
 * question templates generated based on game data.
 *
 * @author machm
 */
class GenreChallengeServiceTest {

    private final GenreChallengeService service = new GenreChallengeService();

    /**
     * Tests logic for Template 0 (Binary choice: Is this game an RPG?).
     * <p>
     * Verifies that if template 0 is selected, the options size is 2 and
     * the answer is "Yes" or "No".
     */
    @Test
    void testCreateQuestionFromGame_template0() {
        Question q = service.createQuestionFromGame(1, "Test Game", List.of("RPG", "Action"));
        assertNotNull(q.getTitle());
        assertTrue(q.getTemplateType() >= 0 && q.getTemplateType() <= 4);
        if (q.getTemplateType() == 0) {
            assertEquals(2, q.getOptions().size());
            assertTrue(q.getCorrectAnswer().equals("Yes") || q.getCorrectAnswer().equals("No"));
        }
    }

    /**
     * Tests logic for Template 1 (Open question: What is the genre?).
     * <p>
     * Verifies that if template 1 is selected, no options are provided (open text input?)
     * and the correct answer matches the genre.
     */
    @Test
    void testCreateQuestionFromGame_template1() {
        Question q = service.createQuestionFromGame(1, "Test Game", List.of("Puzzle"));
        if (q.getTemplateType() == 1) {
            assertTrue(q.getOptions().isEmpty());
            assertEquals("Puzzle", q.getCorrectAnswer());
        }
    }

    /**
     * Tests logic for Template 2 (Count question: How many genres?).
     * <p>
     * Verifies that if template 2 is selected, the correct answer is the string representation
     * of the number of genres (e.g., "2").
     */
    @Test
    void testCreateQuestionFromGame_template2() {
        Question q = service.createQuestionFromGame(1, "Test Game", List.of("FPS", "Action"));
        if (q.getTemplateType() == 2) {
            assertEquals("2", q.getCorrectAnswer());
        }
    }

    /**
     * Tests logic for Template 3 (Binary check: Does it contain genre X?).
     * <p>
     * Verifies that if template 3 is selected, options are Yes/No.
     */
    @Test
    void testCreateQuestionFromGame_template3() {
        Question q = service.createQuestionFromGame(1, "Test Game", List.of("RPG", "Strategy"));
        if (q.getTemplateType() == 3) {
            assertEquals(2, q.getOptions().size());
            assertTrue(q.getCorrectAnswer().equals("Yes") || q.getCorrectAnswer().equals("No"));
        }
    }

    /**
     * Tests logic for Template 4 (Confirmation: Is 'Racing' the primary genre?).
     * <p>
     * Verifies that if template 4 is selected, the answer is "Yes" when the genre matches.
     */
    @Test
    void testCreateQuestionFromGame_template4() {
        Question q = service.createQuestionFromGame(1, "Test Game", List.of("Racing"));
        if (q.getTemplateType() == 4) {
            assertEquals(2, q.getOptions().size());
            assertEquals("Yes", q.getCorrectAnswer());
        }
    }
}