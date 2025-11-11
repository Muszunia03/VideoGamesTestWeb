package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Service
public class RatingEstimatorQuizService {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

    public List<Question> getRatingEstimatorQuestions() {
        List<Question> questions = new ArrayList<>();

        String query = """
            SELECT id, title, rating
            FROM games
            WHERE rating IS NOT NULL
            ORDER BY RANDOM()
            LIMIT 10
        """;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                float rating = rs.getFloat("rating");

                Question q = new Question();
                q.setId(id);
                q.setTitle(title);
                q.setGameId(id);
                q.setTemplateType(1);
                q.setQuestionText("Czy gra '" + title + "' ma ocenę powyżej 3.3?");
                q.setOptions(List.of("tak", "nie"));
                q.setCorrectAnswer(rating > 3.3 ? "tak" : "nie");

                questions.add(q);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }

    public void saveResult(int userId, int lastGameId, int score, int timeTakenSeconds) {
        String insertQuery = """
            INSERT INTO results (user_id, game_id, score, time_taken_seconds)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            insertStmt.setInt(1, userId);
            if (lastGameId != -1) {
                insertStmt.setInt(2, lastGameId);
            } else {
                insertStmt.setNull(2, Types.INTEGER);
            }
            insertStmt.setInt(3, score);
            insertStmt.setInt(4, timeTakenSeconds);

            insertStmt.executeUpdate();
            System.out.println("Wynik zapisany do bazy.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
