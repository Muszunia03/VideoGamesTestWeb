package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Question;
import com.quizapp.quiz.backend.service.NewReleasesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link NewReleasesController} class.
 * <p>
 * This class tests the endpoints responsible for the "New Releases" quiz category.
 * It uses a stubbed {@link NewReleasesService} to simulate the retrieval of questions
 * related to recently released games.
 *
 * @author machm
 */
class NewReleasesControllerTest {

    private NewReleasesService service;
    private NewReleasesController controller;

    /**
     * Sets up the test environment before each test execution.
     * <p>
     * Initializes the controller with an anonymous subclass of {@link NewReleasesService}.
     * This stub provides a deterministic response ("New release question?") to ensure
     * repeatable test results.
     */
    @BeforeEach
    void setUp() {
        service = new NewReleasesService() {
            @Override
            public Question getRandomQuestion() {
                Question q = new Question();
                q.setQuestionText("New release question?");
                q.setOptions(Arrays.asList("A", "B", "C", "D"));
                q.setCorrectAnswer("B");
                return q;
            }
        };
        controller = new NewReleasesController(service);
    }

    /**
     * Verifies that the {@code startQuiz} endpoint correctly initiates the quiz
     * and returns the first question.
     */
    @Test
    void testStartQuiz_ReturnsQuestion() {
        Question question = controller.startQuiz();
        assertNotNull(question);
        assertEquals("New release question?", question.getQuestionText());
        assertEquals(4, question.getOptions().size());
        assertEquals("B", question.getCorrectAnswer());
    }

    /**
     * Verifies that the {@code nextQuestion} endpoint retrieves the subsequent
     * question from the service.
     */
    @Test
    void testNextQuestion_ReturnsQuestion() {
        Question question = controller.nextQuestion();
        assertNotNull(question);
        assertEquals("New release question?", question.getQuestionText());
        assertEquals(4, question.getOptions().size());
        assertEquals("B", question.getCorrectAnswer());
    }
}