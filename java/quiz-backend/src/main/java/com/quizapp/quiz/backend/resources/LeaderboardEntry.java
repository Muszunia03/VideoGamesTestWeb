package com.quizapp.quiz.backend.resources;

/**
 * Data Transfer Object (DTO) representing a single entry on the leaderboard.
 * <p>
 * This resource is used to display player rankings, including their username, score, and the quiz type.
 *
 * @author machm
 */
public class LeaderboardEntry {

    /** The username of the player. */
    private String username;

    /** The score of the player. */
    private int score;

    /** The type of quiz the score relates to (optional, depending on ranking method). */
    private String quizType;

    /**
     * Constructs a new LeaderboardEntry.
     *
     * @param username The name of the player.
     * @param score    The player's score.
     * @param quizType The type of quiz (e.g., "Retro", "Latest Releases").
     */
    public LeaderboardEntry(String username, int score, String quizType) {
        this.username = username;
        this.score = score;
        this.quizType = quizType;
    }

    /**
     * Gets the username of the player.
     *
     * @return The username.
     */
    public String getUsername() { return username; }

    /**
     * Gets the score.
     *
     * @return The player's score.
     */
    public int getScore() { return score; }

    /**
     * Gets the quiz type associated with the score.
     *
     * @return The quiz type string.
     */
    public String getQuizType() { return quizType; }
}