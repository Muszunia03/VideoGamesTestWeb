package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.UserProfileDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Service class for handling user-specific data retrieval and profile management.
 * <p>
 * Uses {@link JdbcTemplate} for database interactions to fetch user IDs and profile details.
 *
 * @author machm
 */
@Service
public class UserService {

    private final JdbcTemplate jdbc;

    /**
     * Constructs the UserService, injecting the Spring {@link JdbcTemplate}.
     *
     * @param jdbc The configured JdbcTemplate instance for database access.
     */
    public UserService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Retrieves the internal user ID based on the provided username (case-insensitive).
     *
     * @param username The username to look up.
     * @return The unique user ID (Integer), or null if the user is not found.
     */
    public Integer getUserIdByUsername(String username) {
        try {
            return jdbc.queryForObject(
                    "SELECT id FROM users WHERE LOWER(username) = LOWER(?)",
                    Integer.class,
                    username
            );
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Nie znaleziono użytkownika: '" + username + "'");
            return null;
        }
    }

    /**
     * Retrieves a detailed profile DTO for a specific user (case-insensitive username).
     * <p>
     * Aggregates basic user info (username, email, creation date) with calculated data (total score).
     *
     * @param username The username of the profile to retrieve.
     * @return A {@link UserProfileDto} containing the user's profile and aggregated score, or null if the user is not found.
     */
    public UserProfileDto getUserProfile(String username) {
        try {
            return jdbc.queryForObject(
                    "SELECT u.username, u.email, u.created_at, " +
                            "COALESCE(SUM(r.score), 0) AS total_score " +
                            "FROM users u " +
                            "LEFT JOIN results r ON r.user_id = u.id " +
                            "WHERE LOWER(u.username) = LOWER(?) " +
                            "GROUP BY u.id",
                    (rs, rowNum) -> new UserProfileDto(
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("created_at"),
                            rs.getInt("total_score")
                    ),
                    username
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
