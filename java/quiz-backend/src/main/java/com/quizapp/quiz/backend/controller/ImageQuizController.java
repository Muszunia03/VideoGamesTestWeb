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
 * Provides endpoints for fetching games, searching titles,
 * and comparing user guesses against correct answers.
 *
 * @author machm
 */
@RestController
@RequestMapping("/api/imagequiz")
@CrossOrigin(origins = "http://localhost:3000")
public class ImageQuizController {

    /**
     * Service handling logic for the image quiz.
     */
    private final ImageQuizService imageQuizService;

    public ImageQuizController(ImageQuizService imageQuizService) {
        this.imageQuizService = imageQuizService;
    }

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
     *
     * @return A list of all {@link Game} objects.
     */
    @GetMapping("/list")
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(imageQuizService.getAllGamesList());
    }

    /**
     * Searches for game titles that match a given query string.
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
     *
     * @param body A map containing keys "guess" and "correct".
     * @return A {@link ComparisonResponse} with comparison details.
     */
    @PostMapping("/compare")
    public ResponseEntity<ComparisonResponse> compare(@RequestBody Map<String, String> body) {
        String guess = body.get("guess");
        String correct = body.get("correct");
        if (guess == null || correct == null) return ResponseEntity.badRequest().build();

        ComparisonResponse resp = imageQuizService.compareGames(guess, correct);
        return ResponseEntity.ok(resp);
    }
}
