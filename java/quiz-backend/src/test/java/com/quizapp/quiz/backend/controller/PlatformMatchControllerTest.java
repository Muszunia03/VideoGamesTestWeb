package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.PlatformMatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PlatformMatchController} class.
 * <p>
 * This class validates the logic for the "Platform Match" quiz mode, ensuring
 * that questions regarding game platforms are correctly retrieved from the service.
 *
 * @author machm
 */
class PlatformMatchControllerTest {

    private PlatformMatchService service;
    private PlatformMatchController controller;

    /**
     * Sets up the test environment before each test execution.
     * <p>
     * Creates a stub implementation of {@link PlatformMatchService} that returns
     * a specific question ("Which platform is this game on?") with a known correct answer ("PS5").
     */
    @BeforeEach
    void setUp() {
        service = new PlatformMatchService() {
            @Override
            public Question getSinglePlatformQuestion() {
                Question q = new Question();
                q.setQuestionText("Which platform is this game on?");
                q.setOptions(Arrays.asList("PC", "PS5", "Switch", "Xbox"));
                q.setCorrectAnswer("PS5");
                return q;
            }
        };
        controller = new PlatformMatchController(service);
    }

    /**
     * Verifies that the {@code nextQuestion} endpoint correctly returns a platform-related question.
     * <p>
     * Asserts that the question text, options list size, and correct answer match
     * the values provided by the service stub.
     */
    @Test
    void testNextQuestion_ReturnsQuestion() {
        Question question = controller.nextQuestion();
        assertNotNull(question);
        assertEquals("Which platform is this game on?", question.getQuestionText());
        assertEquals(4, question.getOptions().size());
        assertEquals("PS5", question.getCorrectAnswer());
    }
}