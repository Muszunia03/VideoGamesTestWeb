package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.resources.LeaderboardEntry;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for persistence and retrieval of quiz results and leaderboard data.
 * <p>
 * Manages saving user scores and fetching ranked lists from the database.
 *
 * @author machm
 */
@Service
public class ResultService {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

    /**
     * Saves a user's quiz result (score and quiz type) to the database.
     *
     * @param userId   The ID of the user who achieved the score.
     * @param score    The score obtained in the quiz.
     * @param quizType The type or category of the quiz played.
     */
    public void saveResult(int userId, int score, String quizType) {
        String query = "INSERT INTO results_quiz (user_id, score, quiz_type) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, score);
            stmt.setString(3, quizType);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the top 20 entries for the global leaderboard, ordered by score descending.
     *
     * @return A list of {@link LeaderboardEntry} DTOs.
     */
    public List<LeaderboardEntry> getLeaderboard() {
        List<LeaderboardEntry> list = new ArrayList<>();
        String query = """
            SELECT u.username, r.score, r.quiz_type
            FROM results_quiz r
            JOIN users u ON u.id = r.user_id
            ORDER BY r.score DESC
            LIMIT 20
        """;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new LeaderboardEntry(
                        rs.getString("username"),
                        rs.getInt("score"),
                        rs.getString("quiz_type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
