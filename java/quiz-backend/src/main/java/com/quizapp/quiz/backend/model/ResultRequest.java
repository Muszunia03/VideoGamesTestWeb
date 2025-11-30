package com.quizapp.quiz.backend.model;

public class ResultRequest {
    private String username;
    private int score;
    private String quizType;

    public ResultRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getQuizType() { return quizType; }
    public void setQuizType(String quizType) { this.quizType = quizType; }
}
