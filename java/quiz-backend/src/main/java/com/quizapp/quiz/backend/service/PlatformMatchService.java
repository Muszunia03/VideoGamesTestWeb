package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Service
public class PlatformMatchService {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

    public List<Question> getPlatformMatchQuestions() {
        List<Question> questions = new ArrayList<>();

        String query = """
            SELECT id, title, platforms
            FROM games
            WHERE array_length(platforms, 1) > 0
            ORDER BY RANDOM()
            LIMIT 10
        """;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Question q = new Question();
                q.setGameId(rs.getInt("id"));
                q.setTitle(rs.getString("title"));

                Array platformsArray = rs.getArray("platforms");
                List<String> platforms = Arrays.asList((String[]) platformsArray.getArray());

                q.setQuestionText("Na jakiej platformie zadebiutowała gra '" + q.getTitle() + "'?");
                q.setOptions(generateOptions(platforms)); // losowe odpowiedzi
                q.setCorrectAnswer(platforms.get(0));
                q.setTemplateType(1);

                questions.add(q);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }

    private List<String> generateOptions(List<String> correctPlatforms) {
        return new ArrayList<>(correctPlatforms);
    }

    public void saveResult(int userId, int gameId, int score, int seconds) {
        String sql = "INSERT INTO results (user_id, game_id, score, time_taken_seconds) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            if (gameId != -1) stmt.setInt(2, gameId);
            else stmt.setNull(2, Types.INTEGER);

            stmt.setInt(3, score);
            stmt.setInt(4, seconds);
            stmt.executeUpdate();

            System.out.println("Wynik zapisany do bazy.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
