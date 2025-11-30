package com.quizapp.quiz.backend.resources;

public class LeaderboardEntry {
    private String username;
    private int score;
    private String quizType;

    public LeaderboardEntry(String username, int score, String quizType) {
        this.username = username;
        this.score = score;
        this.quizType = quizType;
    }

    public String getUsername() { return username; }
    public int getScore() { return score; }
    public String getQuizType() { return quizType; }
}
