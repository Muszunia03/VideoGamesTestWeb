package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.UserProfileDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * Blocks (bans) a user in the system.
     * <p>
     * The method updates the record in the user table, setting the {@code is_banned} flag to {@code true}.
     * The user search is case-insensitive (using the {@code LOWER} function).
     *
     * @param username The username (login) to be blocked.
     */
    public void banUser(String username) {
        jdbc.update("UPDATE users SET is_banned = TRUE WHERE LOWER(username) = LOWER(?)", username);
    }

    /**
     * Unblocks (removes the ban) a user in the system.
     * <p>
     * The method updates the record in the user table by setting the {@code is_banned} flag to {@code false}.
     * The user search is case-insensitive.
     *
     * @param username The username (login) to be unblocked.
     */
    public void unbanUser(String username) {
        jdbc.update("UPDATE users SET is_banned = FALSE WHERE LOWER(username) = LOWER(?)", username);
    }

    /**
     * Retrieves a list of all users registered in the system along with their statistics.
     * <p>
     * The method performs a query joining the user table with the results table (LEFT JOIN)
     * to calculate the total score for each player.
     * <p>
     * The returned DTO object contains:
     * <ul>
     * <li>Username, email, and account creation date.</li>
     * <li>Total score (if there are no results, 0 is returned thanks to {@code COALESCE}).</li>
     * <li>Block status (whether the user is banned).</li>
     * </ul>
     * Results are sorted alphabetically by username.
     *
     * @return a list of {@link UserProfileDto} objects representing the profiles of all users.
     */

    public List<UserProfileDto> getAllUsers() {
        return jdbc.query(
                "SELECT username, email, created_at, COALESCE(SUM(r.score), 0) AS total_score, is_banned " +
                        "FROM users u LEFT JOIN results r ON r.user_id = u.id " +
                        "GROUP BY u.id ORDER BY username",
                (rs, rowNum) -> new UserProfileDto(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("created_at"),
                        rs.getInt("total_score"),
                        rs.getBoolean("is_banned")
                )
        );
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
     * Retrieves a detailed user profile along with their total score.
     * <p>
     * The method performs a query joining the user table with the results table (LEFT JOIN)
     * to calculate the total score for a given user. Additionally, it retrieves the account lock status.
     *
     * @param username User name (case-insensitive).
     * @return An {@link UserProfileDto} object containing the user profile, total points, and lock status,
     *         or {@code null} if the user does not exist.
     */
    public UserProfileDto getUserProfile(String username) {
        try {
            return jdbc.queryForObject(
                    "SELECT u.username, u.email, u.created_at, u.is_banned, " +
                            "COALESCE(SUM(r.score), 0) AS total_score " +
                            "FROM users u " +
                            "LEFT JOIN results r ON r.user_id = u.id " +
                            "WHERE LOWER(u.username) = LOWER(?) " +
                            "GROUP BY u.id",
                    (rs, rowNum) -> new UserProfileDto(
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("created_at"),
                            rs.getInt("total_score"),
                            rs.getBoolean("is_banned")
                    ),
                    username
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
