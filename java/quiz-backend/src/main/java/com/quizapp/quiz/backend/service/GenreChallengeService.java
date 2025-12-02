package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

/**
 * Service class dedicated to generating questions for the "Genre Challenge" quiz.
 * <p>
 * Fetches game data from the database and constructs different types of questions
 * based on the game's genres.
 *
 * @author machm
 */
@Service
public class GenreChallengeService {

    /** A comprehensive list of all possible genres used for question generation. */
    private static final List<String> ALL_GENRES = Arrays.asList(
            "RPG", "FPS", "Action", "Adventure", "Strategy",
            "Simulation", "Puzzle", "Platformer", "Sports", "Racing"
    );

    private final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

    private final Random random = new Random();

    /**
     * Retrieves a single random game with genre information and converts it into a quiz question.
     *
     * @return A {@link Question} object ready to be displayed to the user, or null if no suitable game is found.
     */
    public Question getRandomQuestion() {
        String query = "SELECT id, title, genres FROM games WHERE array_length(genres, 1) > 0 ORDER BY RANDOM() LIMIT 1";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int gameId = rs.getInt("id");
                String title = rs.getString("title");
                Array genresArray = rs.getArray("genres");
                List<String> genres = genresArray != null ? Arrays.asList((String[]) genresArray.getArray()) : List.of();

                return createQuestionFromGame(gameId, title, genres);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Creates a specific type of question based on a randomly selected template (0-4).
     *
     * @param gameId The ID of the game the question is about.
     * @param title The title of the game.
     * @param genres The list of genres associated with the game.
     * @return A fully populated {@link Question} object.
     */
    private Question createQuestionFromGame(int gameId, String title, List<String> genres) {
        Question q = new Question();
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
                q.setCorrectAnswer(genres.isEmpty() ? "" : genres.get(0));
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
                String mainGenre = genres.isEmpty() ? "" : genres.get(0);
                q.setQuestionText("Is '" + mainGenre + "' the primary genre for '" + title + "'?");
                q.setOptions(Arrays.asList("Yes", "No"));
                q.setCorrectAnswer("Yes");
            }
        }
        return q;
    }
}
