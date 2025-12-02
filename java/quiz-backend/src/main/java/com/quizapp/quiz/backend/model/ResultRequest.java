package com.quizapp.quiz.backend.model;

/**
 * Data Transfer Object (DTO) for submitting quiz results.
 * <p>
 * Used by the client to send the final score and quiz type after finishing a game.
 *
 * @author machm
 */
public class ResultRequest {

    /** The username of the player. */
    private String username;

    /** The final score achieved. */
    private int score;

    /** The category or type of the quiz played (e.g., "Retro", "Genre"). */
    private String quizType;

    /**
     * Default, no-argument constructor.
     */
    public ResultRequest() {}

    /**
     * Returns the username of the user who achieved this result.
     *
     * @return The username.
     */
    public String getUsername() { return username; }

    /**
     * Sets the username of the user who achieved this result.
     *
     * @param username The new username.
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Returns the score achieved (number of points).
     *
     * @return The score as an integer.
     */
    public int getScore() { return score; }

    /**
     * Sets the score achieved (number of points).
     *
     * @param score The new score (number of points).
     */
    public void setScore(int score) { this.score = score; }

    /**
     * Returns the type of the quiz or game the result is from (e.g., "Math", "History").
     *
     * @return The quiz/game type as a String.
     */
    public String getQuizType() { return quizType; }

    /**
     * Sets the type of the quiz or game the result is from.
     *
     * @param quizType The new quiz/game type.
     */
    public void setQuizType(String quizType) { this.quizType = quizType; }
}