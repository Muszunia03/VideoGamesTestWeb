/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quizapp.quiz.backend.service;

/**
 *
 * @author machm
 */
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

    public List<Question> getRetroQuestions() {
        List<Question> questions = new ArrayList<>();

        String query = """
            SELECT id, title, release_date, rating, genres, platforms
            FROM games
            WHERE release_date < '2010-01-01'
            AND array_length(platforms, 1) > 0
            ORDER BY RANDOM()
            LIMIT 5
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
                q.setTemplateType(template);

                switch (template) {
                    case 0 -> {
                        q.setQuestionText("Na jakiej platformie zadebiutowała gra '" + title + "'?");
                        q.setOptions(platforms);
                        q.setCorrectAnswer(platforms.isEmpty() ? "" : platforms.get(0));
                    }
                    case 1 -> {
                        q.setQuestionText("Czy gra '" + title + "' została wydana przed 2005 rokiem?");
                        q.setOptions(List.of("tak", "nie"));
                        q.setCorrectAnswer(releaseDate.isBefore(LocalDate.of(2005, 1, 1)) ? "tak" : "nie");
                    }
                    case 2 -> {
                        q.setQuestionText("Czy gra '" + title + "' miała ocenę powyżej 3.3?");
                        q.setOptions(List.of("tak", "nie"));
                        q.setCorrectAnswer(rating > 3.3 ? "tak" : "nie");
                    }
                    case 3 -> {
                        q.setQuestionText("Czy gra '" + title + "' należy do gatunku RPG?");
                        q.setOptions(List.of("tak", "nie"));
                        boolean isRPG = genres.stream().anyMatch(g -> g.equalsIgnoreCase("RPG"));
                        q.setCorrectAnswer(isRPG ? "tak" : "nie");
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
            System.out.println("Wynik zapisany do bazy.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}