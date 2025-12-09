package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.GenreChallengeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link GenreChallengeController} class.
 * <p>
 * This class verifies the functionality of the genre challenge endpoints.
 * It uses a stubbed implementation of {@link GenreChallengeService} to simulate
 * backend behavior without relying on a real database connection or complex logic.
 *
 * @author machm
 */
class GenreChallengeControllerTest {

    private GenreChallengeController controller;

    /**
     * Sets up the test environment before each test execution.
     * <p>
     * Initializes the {@link GenreChallengeController} with an anonymous subclass (stub)
     * of {@link GenreChallengeService}. This stub overrides {@code getRandomQuestion()}
     * to return a deterministic {@link Question} object with fixed data:
     * <ul>
     * <li>Question text: "Test Question"</li>
     * <li>Options: "A", "B", "C", "D"</li>
     * <li>Correct answer: "A"</li>
     * </ul>
     */
    @BeforeEach
    void setUp() {
        GenreChallengeService genreChallengeService = new GenreChallengeService() {
            @Override
            public Question getRandomQuestion() {
                Question q = new Question();
                q.setQuestionText("Test Question");
                q.setOptions(Arrays.asList("A", "B", "C", "D"));
                q.setCorrectAnswer("A");
                return q;
            }
        };
        controller = new GenreChallengeController(genreChallengeService);
    }

    /**
     * Verifies that the {@code getNextQuestion} endpoint correctly retrieves and returns a question.
     * <p>
     * This test asserts that:
     * <ul>
     * <li>The returned object is not null.</li>
     * <li>The question text matches the value from the service stub.</li>
     * <li>The options list matches the expected list.</li>
     * <li>The correct answer matches the expected value.</li>
     * </ul>
     */
    @Test
    void testGetNextQuestion_ReturnsQuestion() {
        Question question = controller.getNextQuestion();

        assertNotNull(question, "Question should not be null");
        assertEquals("Test Question", question.getQuestionText(), "Question text should match");
        assertEquals(Arrays.asList("A", "B", "C", "D"), question.getOptions(), "Options should match");
        assertEquals("A", question.getCorrectAnswer(), "Correct answer should match");
    }
}