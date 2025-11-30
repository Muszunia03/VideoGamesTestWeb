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

    // Zwraca jedno losowe pytanie
    public Question getNextQuestion() {
        String query = """
            SELECT id, title, release_date, rating, genres, platforms
            FROM games
            WHERE release_date IS NOT NULL
              AND rating IS NOT NULL
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

                int template = new Random().nextInt(7); // 0-6
                Question q = new Question();
                q.setId(id);
                q.setTitle(title);
                q.setGameId(id);
                q.setTemplateType(template);

                switch (template) {
                    case 0 -> { // Czy wydana przed 2010
                        q.setQuestionText("Czy gra '" + title + "' została wydana przed 2010 rokiem?");
                        q.setOptions(List.of("tak", "nie"));
                        q.setCorrectAnswer(releaseDate.isBefore(LocalDate.of(2010, 1, 1)) ? "tak" : "nie");
                    }
                    case 1 -> { // Ocena powyżej 4.0
                        q.setQuestionText("Czy gra '" + title + "' ma ocenę powyżej 4.0?");
                        q.setOptions(List.of("tak", "nie"));
                        q.setCorrectAnswer(rating > 4.0 ? "tak" : "nie");
                    }
                    case 2 -> { // Gatunek Action
                        q.setQuestionText("Czy gra '" + title + "' należy do gatunku Action?");
                        q.setOptions(List.of("tak", "nie"));
                        boolean isAction = genres.stream().anyMatch(g -> g.equalsIgnoreCase("Action"));
                        q.setCorrectAnswer(isAction ? "tak" : "nie");
                    }
                    case 3 -> { // Platforma debiutu
                        q.setQuestionText("Na jakiej platformie zadebiutowała gra '" + title + "'?");
                        q.setOptions(platforms); // jeśli jest pusta lista, frontend użyje input box
                        q.setCorrectAnswer(platforms.isEmpty() ? "" : platforms.get(0));
                    }
                    case 4 -> { // Dekada premiery
                        int year = releaseDate.getYear();
                        q.setQuestionText("W której dekadzie wydano grę '" + title + "'?");
                        q.setOptions(List.of("2000-2009", "2010-2019", "2020-2029"));
                        String decade = (year < 2010) ? "2000-2009" : (year < 2020 ? "2010-2019" : "2020-2029");
                        q.setCorrectAnswer(decade);
                    }
                    case 5 -> { // Gatunek losowy
                        List<String> possibleGenres = List.of("Action", "RPG", "Shooter", "Puzzle", "Strategy", "Adventure");
                        String chosenGenre = possibleGenres.get(new Random().nextInt(possibleGenres.size()));
                        q.setQuestionText("Czy gra '" + title + "' należy do gatunku " + chosenGenre + "?");
                        q.setOptions(List.of("tak", "nie"));
                        boolean hasGenre = genres.stream().anyMatch(g -> g.equalsIgnoreCase(chosenGenre));
                        q.setCorrectAnswer(hasGenre ? "tak" : "nie");
                    }
                    case 6 -> { // Platforma losowa z dostępnych
                        if (!platforms.isEmpty()) {
                            Collections.shuffle(platforms);
                            q.setOptions(platforms); // frontend użyje input jeśli lista będzie pusta
                            q.setCorrectAnswer(platforms.get(0));
                            q.setQuestionText("Na której z poniższych platform wydano grę '" + title + "'?");
                        } else {
                            q.setQuestionText("Brak dostępnych platform dla gry '" + title + "'");
                            q.setOptions(List.of());
                            q.setCorrectAnswer("");
                        }
                    }
                }

                return q;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void saveResult(int userId, int lastGameId, int score, int timeTakenSeconds) {
        String insertQuery = """
            INSERT INTO results (user_id, game_id, score, time_taken_seconds)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            insertStmt.setInt(1, userId);
            if (lastGameId != -1) insertStmt.setInt(2, lastGameId);
            else insertStmt.setNull(2, Types.INTEGER);
            insertStmt.setInt(3, score);
            insertStmt.setInt(4, timeTakenSeconds);

            insertStmt.executeUpdate();
            System.out.println("Wynik MultiFactMix zapisany w bazie.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
