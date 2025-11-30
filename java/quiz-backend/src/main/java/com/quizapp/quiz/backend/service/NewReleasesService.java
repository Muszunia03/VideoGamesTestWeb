package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class NewReleasesService {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String password = "admin";

    // Return **one random question** from new releases
    public Question getRandomQuestion() {
        String query = """
            SELECT id, title, release_date, rating, genres, platforms
            FROM games
            WHERE release_date >= '2020-01-01'
            AND array_length(platforms, 1) > 0
            ORDER BY RANDOM()
            LIMIT 1
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Question q = new Question();
                q.setId(rs.getInt("id"));
                q.setTitle(rs.getString("title"));

                LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                float rating = rs.getFloat("rating");
                List<String> genres = toList(rs.getArray("genres"));
                List<String> platforms = toList(rs.getArray("platforms"));

                int questionType = new Random().nextInt(4);
                switch (questionType) {
                    case 0 -> {
                        q.setQuestionText("Was the game '" + q.getTitle() + "' released after 2023?");
                        q.setOptions(List.of("yes", "no"));
                        q.setCorrectAnswer(releaseDate.isAfter(LocalDate.of(2023, 1, 1)) ? "yes" : "no");
                    }
                    case 1 -> {
                        q.setQuestionText("Does '" + q.getTitle() + "' have a rating above 4.0?");
                        q.setOptions(List.of("yes", "no"));
                        q.setCorrectAnswer(rating > 4.0 ? "yes" : "no");
                    }
                    case 2 -> {
                        q.setQuestionText("Is '" + q.getTitle() + "' an Action game?");
                        q.setOptions(List.of("yes", "no"));
                        boolean isAction = genres.stream().anyMatch(g -> g.equalsIgnoreCase("Action"));
                        q.setCorrectAnswer(isAction ? "yes" : "no");
                    }
                    case 3 -> {
                        q.setQuestionText("Which platform did '" + q.getTitle() + "' debut on?");
                        q.setOptions(platforms);
                        q.setCorrectAnswer(platforms.isEmpty() ? "" : platforms.get(0));
                    }
                }

                return q;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<String> toList(Array array) throws SQLException {
        if (array == null) return List.of();
        return Arrays.asList((String[]) array.getArray());
    }
}
