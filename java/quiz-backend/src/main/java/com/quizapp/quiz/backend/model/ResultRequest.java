/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quizapp.quiz.backend.model;

/**
 *
 * @author machm
 */
public class ResultRequest {
    private int userId;
    private int gameId;
    private int score;
    private int timeTakenSeconds;

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getGameId() { return gameId; }
    public void setGameId(int gameId) { this.gameId = gameId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTimeTakenSeconds() { return timeTakenSeconds; }
    public void setTimeTakenSeconds(int timeTakenSeconds) { this.timeTakenSeconds = timeTakenSeconds; }
}
