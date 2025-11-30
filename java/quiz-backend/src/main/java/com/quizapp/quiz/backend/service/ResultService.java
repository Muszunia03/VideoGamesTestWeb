package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.resources.LeaderboardEntry;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResultService {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

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
