package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class MultiFactMixQuizService {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

    public List<Question> getMultiFactMixQuestions() {
        List<Question> questions = new ArrayList<>();

        String query = """
            SELECT id, title, release_date, rating, genres, platforms
            FROM games
            WHERE release_date IS NOT NULL
              AND rating IS NOT NULL
              AND array_length(platforms, 1) > 0
            ORDER BY RANDOM()
            LIMIT 10
        """;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                float rating = rs.getFloat("rating");
                Array genresArray = rs.getArray("genres");
                Array platformsArray = rs.getArray("platforms");

                List<String> genres = genresArray != null ? Arrays.asList((String[]) genresArray.getArray()) : new ArrayList<>();
                List<String> platforms = platformsArray != null ? Arrays.asList((String[]) platformsArray.getArray()) : new ArrayList<>();

                int template = new Random().nextInt(4);
                Question q = new Question();
                q.setId(id);
                q.setTitle(title);
                q.setGameId(id);
                q.setTemplateType(template);

                switch (template) {
                    case 0 -> {
                        q.setQuestionText("Czy gra '" + title + "' została wydana przed 2010 rokiem?");
                        q.setOptions(List.of("tak", "nie"));
                        q.setCorrectAnswer(releaseDate.isBefore(LocalDate.of(2010, 1, 1)) ? "tak" : "nie");
                    }
                    case 1 -> {
                        q.setQuestionText("Czy gra '" + title + "' ma ocenę powyżej 4.0?");
                        q.setOptions(List.of("tak", "nie"));
                        q.setCorrectAnswer(rating > 4.0 ? "tak" : "nie");
                    }
                    case 2 -> {
                        q.setQuestionText("Czy gra '" + title + "' należy do gatunku Akcji?");
                        q.setOptions(List.of("tak", "nie"));
                        boolean isAction = genres.stream().anyMatch(g -> g.equalsIgnoreCase("Action"));
                        q.setCorrectAnswer(isAction ? "tak" : "nie");
                    }
                    case 3 -> {
                        q.setQuestionText("Na jakiej platformie zadebiutowała gra '" + title + "'?");
                        q.setOptions(platforms);
                        q.setCorrectAnswer(platforms.isEmpty() ? "" : platforms.get(0));
                    }
                }

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
            System.out.println("Wynik MultiFactMix zapisany w bazie.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
