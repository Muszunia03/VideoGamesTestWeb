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

    public List<Question> getNewReleaseQuestions() {
        List<Question> questions = new ArrayList<>();

        String query = """
            SELECT id, title, release_date, rating, genres, platforms
            FROM games
            WHERE release_date >= '2020-01-01'
            AND array_length(platforms, 1) > 0
            ORDER BY RANDOM()
            LIMIT 10
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            Random random = new Random();

            while (rs.next()) {
                Question q = new Question();
                q.setId(rs.getInt("id"));
                q.setTitle(rs.getString("title"));

                LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                float rating = rs.getFloat("rating");
                List<String> genres = toList(rs.getArray("genres"));
                List<String> platforms = toList(rs.getArray("platforms"));

                int questionType = random.nextInt(4);
                switch (questionType) {
                    case 0 -> {
                        q.setQuestionText("Czy gra '" + q.getTitle() + "' została wydana po 2023 roku?");
                        q.setOptions(List.of("tak", "nie"));
                        q.setCorrectAnswer(releaseDate.isAfter(LocalDate.of(2023, 1, 1)) ? "tak" : "nie");
                    }
                    case 1 -> {
                        q.setQuestionText("Czy gra '" + q.getTitle() + "' ma ocenę powyżej 4.0?");
                        q.setOptions(List.of("tak", "nie"));
                        q.setCorrectAnswer(rating > 4.0 ? "tak" : "nie");
                    }
                    case 2 -> {
                        q.setQuestionText("Czy '" + q.getTitle() + "' to gra akcji?");
                        q.setOptions(List.of("tak", "nie"));
                        boolean isAction = genres.stream().anyMatch(g -> g.equalsIgnoreCase("Action"));
                        q.setCorrectAnswer(isAction ? "tak" : "nie");
                    }
                    case 3 -> {
                        q.setQuestionText("Na jakiej platformie zadebiutowała gra '" + q.getTitle() + "'?");
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

    private List<String> toList(Array array) throws SQLException {
        if (array == null) return List.of();
        return Arrays.asList((String[]) array.getArray());
    }
}
