package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.resources.LeaderboardEntry;
import com.quizapp.quiz.backend.service.ResultService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@CrossOrigin(origins = "*")
public class LeaderboardController {

    private final ResultService resultService;

    public LeaderboardController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping
    public List<LeaderboardEntry> getLeaderboard() {
        return resultService.getLeaderboard();
    }
}
