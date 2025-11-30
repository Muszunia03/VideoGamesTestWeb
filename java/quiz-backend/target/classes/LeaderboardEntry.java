package com.quizapp.quiz.backend.resources;

import java.time.LocalDateTime;

public class LeaderboardEntry {
    private String username;
    private int score;
    private String quizType;
    private LocalDateTime createdAt;

    public LeaderboardEntry(String username, int score, String quizType, LocalDateTime createdAt) {
        this.username = username;
        this.score = score;
        this.quizType = quizType;
        this.createdAt = createdAt;
    }

    public String getUsername() { return username; }
    public int getScore() { return score; }
    public String getQuizType() { return quizType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
