package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.MultiFactMixQuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link MultiFactMixQuizController} class.
 * <p>
 * This class validates the behavior of the "Multi Fact Mix" quiz endpoints.
 * It ensures that the controller correctly delegates question retrieval to the
 * service and handles both valid returns and null responses (e.g., when questions are exhausted).
 *
 * @author machm
 */
class MultiFactMixQuizControllerTest {

    private MultiFactMixQuizService service;
    private MultiFactMixQuizController controller;

    /**
     * Sets up the test environment before each test execution.
     * <p>
     * Initializes the {@link MultiFactMixQuizController} with a default stub of
     * {@link MultiFactMixQuizService}. This default stub returns a fixed sample question
     * to verify basic functionality.
     */
    @BeforeEach
    void setUp() {
        service = new MultiFactMixQuizService() {
            @Override
            public Question getNextQuestion() {
                Question question = new Question();
                question.setQuestionText("Sample question?");
                question.setOptions(Arrays.asList("A", "B", "C", "D"));
                question.setCorrectAnswer("A");
                return question;
            }
        };

        controller = new MultiFactMixQuizController(service);
    }

    /**
     * Verifies that the {@code getNextQuestion} endpoint returns a valid {@link Question} object
     * with correct fields when the service provides data.
     */
    @Test
    void testGetNextQuestion_ReturnsQuestion() {
        Question response = controller.getNextQuestion();

        assertNotNull(response);
        assertEquals("Sample question?", response.getQuestionText());
        assertEquals(4, response.getOptions().size());
        assertEquals("A", response.getCorrectAnswer());
    }

    /**
     * Verifies the controller's behavior when the service returns {@code null}.
     * <p>
     * This scenario might occur if there are no more questions available in the pool.
     * The test overrides the default service stub to return {@code null}.
     */
    @Test
    void testGetNextQuestion_NullQuestion() {
        service = new MultiFactMixQuizService() {
            @Override
            public Question getNextQuestion() {
                return null;
            }
        };
        controller = new MultiFactMixQuizController(service);

        Question response = controller.getNextQuestion();
        assertNull(response);
    }
}