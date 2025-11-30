package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.model.Game;
import com.quizapp.quiz.backend.service.ImageQuizService;
import com.quizapp.quiz.backend.service.ImageQuizService.ComparisonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/imagequiz")
@CrossOrigin(origins = "http://localhost:3000")
public class ImageQuizController {

    private final ImageQuizService imageQuizService = new ImageQuizService();

    @GetMapping("/random")
    public ResponseEntity<Game> getRandomGame() {
        Game game = imageQuizService.getRandomGame();
        if (game != null) return ResponseEntity.ok(game);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(imageQuizService.getAllGamesList());
    }

    @GetMapping("/search")
    public ResponseEntity<List<String>> searchGames(@RequestParam("query") String query) {
        return ResponseEntity.ok(imageQuizService.searchGamesByTitle(query));
    }

    @PostMapping("/compare")
    public ResponseEntity<ComparisonResponse> compare(@RequestBody Map<String, String> body) {
        String guess = body.get("guess");
        String correct = body.get("correct");
        if (guess == null || correct == null) return ResponseEntity.badRequest().build();

        ComparisonResponse resp = imageQuizService.compareGames(guess, correct);
        return ResponseEntity.ok(resp);
    }

    // legacy check (kept for compatibility)
    @PostMapping("/check")
    public ResponseEntity<?> checkAnswer(@RequestBody GuessRequest request) {
        boolean correct = request.getGuess().equalsIgnoreCase(request.getCorrect());
        return ResponseEntity.ok(new GuessResponse(correct));
    }

    public static class GuessRequest {
        private String guess;
        private String correct;
        public String getGuess() { return guess; }
        public void setGuess(String guess) { this.guess = guess; }
        public String getCorrect() { return correct; }
        public void setCorrect(String correct) { this.correct = correct; }
    }

    public static class GuessResponse {
        private boolean correct;
        public GuessResponse(boolean correct) { this.correct = correct; }
        public boolean isCorrect() { return correct; }
    }
}
