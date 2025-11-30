package com.quizapp.quiz.backend.model;

public class UserProfileDto {
    private String username;
    private String email;
    private String createdAt;
    private Integer totalScore;

    public UserProfileDto(String username, String email, String createdAt, Integer totalScore) {
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
        this.totalScore = totalScore;
    }

    // GETTERS
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getCreatedAt() { return createdAt; }
    public Integer getTotalScore() { return totalScore; }
}
