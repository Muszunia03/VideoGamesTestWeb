package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Service
public class GenreChallengeService {

    private static final List<String> ALL_GENRES = Arrays.asList(
            "RPG", "FPS", "Action", "Adventure", "Strategy",
            "Simulation", "Puzzle", "Platformer", "Sports", "Racing"
    );

    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public GenreChallengeService() {
        // Ideally, load from application.properties
        this.dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        this.dbUser = "postgres";
        this.dbPassword = "admin";
    }

    /**
     * Generate a list of quiz questions for a user.
     * @param limit number of questions to generate
     */
    public List<Question> generateQuestions(int limit) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String query = """
            SELECT id, title, genres 
            FROM games 
            WHERE array_length(genres, 1) > 0
            ORDER BY RANDOM() 
            LIMIT ?
            """;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                int questionId = 1;
                Random random = new Random();

                while (rs.next()) {
                    int gameId = rs.getInt("id");
                    String title = rs.getString("title");
                    Array genresArray = rs.getArray("genres");
                    @SuppressWarnings("unchecked")
                    List<String> genres = Arrays.asList((String[]) genresArray.getArray());

                    questions.add(createQuestionFromGame(gameId, title, genres, questionId++, random));
                }
            }
        }
        return questions;
    }

    private Question createQuestionFromGame(int gameId, String title, List<String> genres, int id, Random random) {
        Question q = new Question();
        q.setId(id);
        q.setGameId(gameId);
        q.setTitle(title);

        int template = random.nextInt(5);
        q.setTemplateType(template);

        switch (template) {
            case 0 -> {
                String genreToAsk = ALL_GENRES.get(random.nextInt(ALL_GENRES.size()));
                q.setQuestionText("Does '" + title + "' belong to the " + genreToAsk + " genre?");
                q.setOptions(Arrays.asList("Yes", "No"));
                q.setCorrectAnswer(genres.contains(genreToAsk) ? "Yes" : "No");
            }
            case 1 -> {
                q.setQuestionText("Name one genre for the game: '" + title + "'");
                q.setOptions(Collections.emptyList());
                q.setCorrectAnswer(genres.get(0));
            }
            case 2 -> {
                q.setQuestionText("How many primary genres does '" + title + "' have?");
                q.setOptions(Collections.emptyList());
                q.setCorrectAnswer(String.valueOf(genres.size()));
            }
            case 3 -> {
                List<String> shuffled = new ArrayList<>(ALL_GENRES);
                Collections.shuffle(shuffled);
                String g1 = shuffled.get(0);
                String g2 = shuffled.get(1);
                q.setQuestionText("Does '" + title + "' belong to both the '" + g1 + "' and '" + g2 + "' genres?");
                q.setOptions(Arrays.asList("Yes", "No"));
                q.setCorrectAnswer((genres.contains(g1) && genres.contains(g2)) ? "Yes" : "No");
            }
            case 4 -> {
                String mainGenre = genres.get(0);
                q.setQuestionText("Is '" + mainGenre + "' the primary genre for '" + title + "'?");
                q.setOptions(Arrays.asList("Yes", "No"));
                q.setCorrectAnswer("Yes");
            }
        }
        return q;
    }

    /**
     * Save quiz result for a user.
     */
    public void saveResult(int userId, int lastGameId, int score, int durationSeconds) {
        String sql = "INSERT INTO results (user_id, game_id, score, time_taken_seconds) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            if (lastGameId != -1) stmt.setInt(2, lastGameId); else stmt.setNull(2, Types.INTEGER);
            stmt.setInt(3, score);
            stmt.setInt(4, durationSeconds);
            stmt.executeUpdate();

            System.out.println("Result saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
