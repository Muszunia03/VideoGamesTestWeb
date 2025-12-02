package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.resources.LeaderboardEntry;
import com.quizapp.quiz.backend.service.ResultService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for managing and retrieving leaderboard data.
 * <p>
 * Provides endpoints to access player rankings and scores.
 * Accessible via /api/leaderboard.
 *
 * @author machm
 */
@RestController
@RequestMapping("/api/leaderboard")
@CrossOrigin(origins = "*")
public class LeaderboardController {

    private final ResultService resultService;

    /**
     * Constructs the LeaderboardController with the necessary ResultService.
     *
     * @param resultService Service responsible for aggregating and retrieving user results.
     */
    public LeaderboardController(ResultService resultService) {
        this.resultService = resultService;
    }

    /**
     * Retrieves the current leaderboard rankings.
     *
     * @return A list of {@link LeaderboardEntry} objects representing the top players and their scores.
     */
    @GetMapping
    public List<LeaderboardEntry> getLeaderboard() {
        return resultService.getLeaderboard();
    }
}