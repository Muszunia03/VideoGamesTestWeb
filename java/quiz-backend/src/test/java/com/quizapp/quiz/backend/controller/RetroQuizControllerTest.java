package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.RetroQuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link RetroQuizController} class.
 * <p>
 * Tests the functionality of the "Retro Quiz" endpoint.
 * Ensures the controller properly retrieves questions from the {@link RetroQuizService}.
 *
 * @author machm
 */
class RetroQuizControllerTest {

    private RetroQuizService service;
    private RetroQuizController controller;

    /**
     * Sets up the test environment.
     * <p>
     * Initializes the controller with a stubbed {@link RetroQuizService} that returns
     * a specific retro-themed question for validation purposes.
     */
    @BeforeEach
    void setUp() {
        service = new RetroQuizService() {
            @Override
            public Question getNextRetroQuestion() {
                Question q = new Question();
                q.setQuestionText("Retro game question?");
                q.setOptions(Arrays.asList("A", "B", "C", "D"));
                q.setCorrectAnswer("C");
                return q;
            }
        };
        controller = new RetroQuizController(service);
    }

    /**
     * Verifies that {@code getNextQuestion} returns a valid question object with the correct data.
     */
    @Test
    void testGetNextQuestion() {
        Question question = controller.getNextQuestion();
        assertNotNull(question);
        assertEquals("Retro game question?", question.getQuestionText());
        assertEquals(4, question.getOptions().size());
        assertEquals("C", question.getCorrectAnswer());
    }
}