package com.quizapp.quiz.backend.model;

/**
 * Data Transfer Object (DTO) representing a user's public profile.
 * <p>
 * Contains aggregated data such as total score and account creation date,
 * safe for frontend display.
 *
 * @author machm
 */
public class UserProfileDto {

    /** The user's display name or unique nickname. */
    private String username;

    /** The user's email address. */
    private String email;

    /** The account creation date formatted as a String. */
    private String createdAt;

    /** The sum of scores from all played quizzes. */
    private Integer totalScore;

    /**
     * Constructs a new UserProfileDto.
     *
     * @param username   The user's display name.
     * @param email      The user's email.
     * @param createdAt  Formatted creation date string.
     * @param totalScore Total accumulated score.
     */
    public UserProfileDto(String username, String email, String createdAt, Integer totalScore) {
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
        this.totalScore = totalScore;
    }

    /**
     * Returns the user's display name or unique nickname.
     *
     * @return The username as a String.
     */
    public String getUsername() { return username; }

    /**
     * Returns the user's email address.
     *
     * @return The email address as a String.
     */
    public String getEmail() { return email; }

    /**
     * Returns the account creation date formatted as a String.
     *
     * @return The formatted account creation date string.
     */
    public String getCreatedAt() { return createdAt; }

    /**
     * Returns the total accumulated score from all played quizzes.
     *
     * @return The total accumulated score as an Integer.
     */
    public Integer getTotalScore() { return totalScore; }
}