package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Game;
import com.quizapp.quiz.backend.service.ImageQuizService;
import com.quizapp.quiz.backend.service.ImageQuizService.ComparisonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller handling the "Image Quiz" game mode (Guess the Game from an image).
 * <p>
 * Provides endpoints for fetching games, searching titles, and comparing user guesses against correct answers.
 *
 * @author machm
 */
@RestController
@RequestMapping("/api/imagequiz")
@CrossOrigin(origins = "http://localhost:3000")
public class ImageQuizController {

    /**
     * An instance of the service responsible for handling business logic and operations
     * related to image-based quizzes.
     * <p>
     * This field is declared as {@code final}, indicating that the reference to the
     * {@code ImageQuizService} is initialized once and cannot be reassigned.
     */
    private final ImageQuizService imageQuizService = new ImageQuizService();

    /**
     * Fetches a random game to display in the quiz.
     *
     * @return A {@link ResponseEntity} containing a {@link Game} object if found,
     * or 204 No Content if no games are available.
     */
    @GetMapping("/random")
    public ResponseEntity<Game> getRandomGame() {
        Game game = imageQuizService.getRandomGame();
        if (game != null) return ResponseEntity.ok(game);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves the complete list of available games.
     * Useful for debugging or listing all possibilities.
     *
     * @return A list of all {@link Game} objects.
     */
    @GetMapping("/list")
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(imageQuizService.getAllGamesList());
    }

    /**
     * Searches for game titles that match a given query string.
     * Used for autocomplete functionality in the frontend.
     *
     * @param query The search string provided by the user.
     * @return A list of matching game titles.
     */
    @GetMapping("/search")
    public ResponseEntity<List<String>> searchGames(@RequestParam("query") String query) {
        return ResponseEntity.ok(imageQuizService.searchGamesByTitle(query));
    }

    /**
     * Compares the user's guess with the correct answer.
     * <p>
     * This endpoint provides detailed logic (likely fuzzy matching or specific criteria)
     * handled by the {@link ImageQuizService}.
     *
     * @param body A map containing keys "guess" and "correct".
     * @return A {@link ComparisonResponse} with details about the accuracy of the guess,
     * or 400 Bad Request if parameters are missing.
     */
    @PostMapping("/compare")
    public ResponseEntity<ComparisonResponse> compare(@RequestBody Map<String, String> body) {
        String guess = body.get("guess");
        String correct = body.get("correct");
        if (guess == null || correct == null) return ResponseEntity.badRequest().build();

        ComparisonResponse resp = imageQuizService.compareGames(guess, correct);
        return ResponseEntity.ok(resp);
    }

    /**
     * Checks if the user's guess matches the correct answer (Legacy/Simple check).
     * <p>
     * This performs a simple case-insensitive string comparison.
     *
     * @param request The {@link GuessRequest} containing the user's guess and the correct answer.
     * @return A {@link GuessResponse} containing a boolean indicating if the answer was correct.
     * @deprecated Use {@link #compare(Map)} for more advanced comparison logic.
     */
    @Deprecated
    @PostMapping("/check")
    public ResponseEntity<?> checkAnswer(@RequestBody GuessRequest request) {
        boolean correct = request.getGuess().equalsIgnoreCase(request.getCorrect());
        return ResponseEntity.ok(new GuessResponse(correct));
    }

    /**
     * DTO for handling simple guess requests.
     */
    /**
     * Represents a Data Transfer Object (DTO) used for carrying a user's guess
     * and the expected correct answer (or key identifying it) during a game or quiz submission.
     * <p>
     * This class is typically used when submitting an answer from a client application to the server.
     */
    public static class GuessRequest {

        /**
         * The value submitted by the user as their guess or answer.
         */
        private String guess;

        /**
         * The actual correct value or an identifier (e.g., ID) that represents
         * the correct answer for the question being guessed.
         */
        private String correct;

        /**
         * Returns the user's submitted guess or answer.
         *
         * @return The guess as a String.
         */
        public String getGuess() { return guess; }

        /**
         * Sets the user's submitted guess or answer.
         *
         * @param guess The new guess value.
         */
        public void setGuess(String guess) { this.guess = guess; }

        /**
         * Returns the correct value or identifier for the question.
         *
         * @return The correct answer/identifier as a String.
         */
        public String getCorrect() { return correct; }

        /**
         * Sets the correct value or identifier for the question.
         *
         * @param correct The new correct answer/identifier.
         */
        public void setCorrect(String correct) { this.correct = correct; }
    }

    /**
     * DTO for sending simple boolean guess responses.
     */
    public static class GuessResponse {
        /**
         * Flag indicating whether the submitted guess was correct (true) or incorrect (false).
         */
        private boolean correct;

        /**
         * Constructs a new GuessResponse object with the specified correctness status.
         *
         * @param correct The boolean status of the guess (true if correct, false otherwise).
         */
        public GuessResponse(boolean correct) {
            this.correct = correct;
        }

        /**
         * Checks if the guess associated with this response was correct.
         *
         * @return {@code true} if the guess was correct, {@code false} otherwise.
         */
        public boolean isCorrect() {
            return correct;
        }
    }
}