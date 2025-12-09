package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Service class dedicated to generating questions for the "New Releases" quiz.
 * <p>
 * Focuses on games released recently (after '2020-01-01') and constructs simple True/False or multiple-choice questions
 * based on their core attributes like release date, rating, genre, and platform.
 *
 * @author machm
 */
@Service
public class NewReleasesService {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String password = "admin";

    /**
     * Retrieves one random game released after 2020-01-01 and generates a question based on a random template (0-3).
     *
     * @return A fully populated {@link Question} object, or null if no suitable game is found.
     */
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

                int questionType = new Random().nextInt(12);
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
                    case 4 -> {
                        List<String> possibleGenres = List.of("Action", "RPG", "Shooter", "Puzzle", "Strategy", "Adventure");
                        String chosenGenre = possibleGenres.get(new Random().nextInt(possibleGenres.size()));
                        q.setQuestionText("Does '" + q.getTitle() + "' belong to the " + chosenGenre + " genre?");
                        q.setOptions(List.of("yes", "no"));
                        boolean hasGenre = genres.stream().anyMatch(g -> g.equalsIgnoreCase(chosenGenre));
                        q.setCorrectAnswer(hasGenre ? "yes" : "no");
                    }
                    case 5 -> {
                        int year = releaseDate.getYear();
                        String range = (year < 2021) ? "2020-2020" : (year < 2022) ? "2021-2021" : "2022+";
                        q.setQuestionText("In which release year range does '" + q.getTitle() + "' belong?");
                        q.setOptions(List.of("2020-2020", "2021-2021", "2022+"));
                        q.setCorrectAnswer(range);
                    }
                    case 6 -> {
                        List<String> allPlatforms = List.of("PC", "PS3", "PS4", "PS5", "Xbox 360", "Xbox One", "Switch");
                        List<String> notReleased = new ArrayList<>(allPlatforms.stream()
                                .filter(p -> platforms.stream().noneMatch(x -> x.equalsIgnoreCase(p)))
                                .toList());
                        if (notReleased.isEmpty()) return null;
                        Collections.shuffle(notReleased);
                        List<String> options = notReleased.subList(0, Math.min(3, notReleased.size()));
                        q.setQuestionText("Which of the following platforms did NOT release '" + q.getTitle() + "'?");
                        q.setOptions(options);
                        q.setCorrectAnswer(options.get(0));
                    }
                    case 7 -> {
                        String otherQuery = """
                            SELECT title, rating
                            FROM games
                            WHERE release_date >= '2020-01-01'
                            ORDER BY RANDOM()
                            LIMIT 1
                        """;
                        try (PreparedStatement s2 = conn.prepareStatement(otherQuery);
                             ResultSet r2 = s2.executeQuery()) {
                            if (!r2.next()) return null;
                            String otherTitle = r2.getString("title");
                            float otherRating = r2.getFloat("rating");

                            q.setQuestionText("Which game has a higher rating: '" + q.getTitle() + "' or '" + otherTitle + "'?");
                            q.setOptions(List.of(q.getTitle(), otherTitle));
                            q.setCorrectAnswer((rating > otherRating) ? q.getTitle() : otherTitle);
                        }
                    }
                    case 8 -> {
                        String randomGenre = genres.isEmpty() ? "Action" : genres.get(0);
                        int year = releaseDate.getYear();
                        q.setQuestionText("Was '" + q.getTitle() + "' released after 2021 and is it a " + randomGenre + " game?");
                        q.setOptions(List.of("yes", "no"));
                        boolean answer = releaseDate.isAfter(LocalDate.of(2021, 12, 31)) && genres.contains(randomGenre);
                        q.setCorrectAnswer(answer ? "yes" : "no");
                    }
                    case 9 -> {
                        String otherQuery = "SELECT title, rating FROM games WHERE release_date >= '2020-01-01' ORDER BY RANDOM() LIMIT 1";
                        try (PreparedStatement s2 = conn.prepareStatement(otherQuery);
                             ResultSet r2 = s2.executeQuery()) {
                            if (!r2.next()) return null;
                            String otherTitle = r2.getString("title");
                            float otherRating = r2.getFloat("rating");

                            q.setQuestionText("Which game has a lower rating: '" + q.getTitle() + "' or '" + otherTitle + "'?");
                            q.setOptions(List.of(q.getTitle(), otherTitle));
                            q.setCorrectAnswer((rating < otherRating) ? q.getTitle() : otherTitle);
                        }
                    }
                    case 10 -> {
                        String otherQuery = "SELECT platforms FROM games WHERE release_date >= '2020-01-01' ORDER BY RANDOM() LIMIT 1";
                        try (PreparedStatement s2 = conn.prepareStatement(otherQuery);
                             ResultSet r2 = s2.executeQuery()) {
                            if (!r2.next()) return null;
                            List<String> otherPlatforms = toList(r2.getArray("platforms"));
                            q.setQuestionText("Does '" + q.getTitle() + "' have more platforms than another random game?");
                            q.setOptions(List.of("yes", "no"));
                            q.setCorrectAnswer(platforms.size() > otherPlatforms.size() ? "yes" : "no");
                        }
                    }
                    case 11 -> {
                        q.setQuestionText("Is '" + q.getTitle() + "' released on at least 3 platforms?");
                        q.setOptions(List.of("yes", "no"));
                        q.setCorrectAnswer(platforms.size() >= 3 ? "yes" : "no");
                    }


                }

                if (q.getOptions() == null || q.getOptions().size() <= 1) {
                    return null;
                }

                return q;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Helper method to safely convert a PostgreSQL SQL Array into a Java List of Strings.
     *
     * @param array The SQL Array object retrieved from the database.
     * @return A List of Strings, or an empty list if the input array is null.
     * @throws SQLException If a database access error occurs during array conversion.
     */
    private List<String> toList(Array array) throws SQLException {
        if (array == null) return List.of();
        return Arrays.asList((String[]) array.getArray());
    }
}
