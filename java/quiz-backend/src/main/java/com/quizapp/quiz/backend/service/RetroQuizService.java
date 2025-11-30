package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class RetroQuizService {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

    // Return a single random retro question
    public Question getNextRetroQuestion() {
        Question q = null;
        String query = """
            SELECT id, title, release_date, rating, genres, platforms
            FROM games
            WHERE release_date < '2010-01-01'
            AND array_length(platforms, 1) > 0
            ORDER BY RANDOM()
            LIMIT 1
        """;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                float rating = rs.getFloat("rating");
                Array genresArray = rs.getArray("genres");
                Array platformsArray = rs.getArray("platforms");

                List<String> genres = genresArray != null ? Arrays.asList((String[]) genresArray.getArray()) : new ArrayList<>();
                List<String> platforms = platformsArray != null ? Arrays.asList((String[]) platformsArray.getArray()) : new ArrayList<>();

                int template = new Random().nextInt(4);
                q = new Question();
                q.setId(id);
                q.setTitle(title);
                q.setTemplateType(template);

                switch (template) {
                    case 0 -> {
                        q.setQuestionText("Which platform did the game '" + title + "' debut on?");
                        q.setOptions(platforms);
                        q.setCorrectAnswer(platforms.isEmpty() ? "" : platforms.get(0));
                    }
                    case 1 -> {
                        q.setQuestionText("Was the game '" + title + "' released before 2005?");
                        q.setOptions(List.of("yes", "no"));
                        q.setCorrectAnswer(releaseDate.isBefore(LocalDate.of(2005, 1, 1)) ? "yes" : "no");
                    }
                    case 2 -> {
                        q.setQuestionText("Did the game '" + title + "' have a rating above 3.3?");
                        q.setOptions(List.of("yes", "no"));
                        q.setCorrectAnswer(rating > 3.3 ? "yes" : "no");
                    }
                    case 3 -> {
                        q.setQuestionText("Is the game '" + title + "' an RPG?");
                        q.setOptions(List.of("yes", "no"));
                        boolean isRPG = genres.stream().anyMatch(g -> g.equalsIgnoreCase("RPG"));
                        q.setCorrectAnswer(isRPG ? "yes" : "no");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return q;
    }
}
