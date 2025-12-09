package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Game;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
/**
 * Service class for the "Image Quiz" game mode (Guess the Game from an image).
 * <p>
 * Handles data retrieval for games, searching, and complex comparison logic
 * used to provide feedback on the user's guess (like Wordle/connections style).
 *
 * @author machm
 */
@Service
public class ImageQuizService {
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

    /**
     * Retrieves a single random game from the database that has a background image available.
     *
     * @return A {@link Game} object with full details, or null if no suitable game is found.
     */
    public Game getRandomGame() {
        String query = """
            SELECT id, external_id, title, description, release_date, background_image, rating, genres, platforms, created_at
            FROM games
            WHERE background_image IS NOT NULL AND background_image != ''
            ORDER BY RANDOM()
            LIMIT 1
        """;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return mapRowToGame(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a list of all games that have an available background image, sorted by title.
     *
     * @return A list of {@link Game} objects.
     */
    public List<Game> getAllGamesList() {
        List<Game> games = new ArrayList<>();
        String query = """
            SELECT id, external_id, title, description, release_date, background_image, rating, genres, platforms, created_at
            FROM games
            WHERE background_image IS NOT NULL AND background_image != ''
            ORDER BY title ASC
        """;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                games.add(mapRowToGame(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    /**
     * Searches for games whose titles match the given query (case-insensitive, partial match).
     * Used typically for autocompletion on the frontend.
     *
     * @param q The search query string.
     * @return A list of matching game titles (up to 20 results).
     */
    public List<String> searchGamesByTitle(String q) {
        List<String> results = new ArrayList<>();
        String sql = "SELECT title FROM games WHERE LOWER(title) LIKE LOWER(?) ORDER BY title LIMIT 20";
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + q + "%";
            stmt.setString(1, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Retrieves a single game by its exact title. Retrieves the data about the correct game and the guessed game
     *
     * @param title The exact title of the game.
     * @return The {@link Game} object, or null if not found.
     */
    public Game getGameByTitle(String title) {
        String sql = """
            SELECT id, external_id, title, description, release_date, background_image, rating, genres, platforms, created_at
            FROM games
            WHERE title = ?
            LIMIT 1
        """;
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRowToGame(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Represents a Data Transfer Object (DTO) used to convey the results of a comparison
     * (e.g., in a guessing game where a user's guess is validated against a target).
     * <p>
     * This class is designed as a static nested class for encapsulation within a main class.
     */
    public static class ComparisonResponse {

        /**
         * The status indicating the correctness of the compared title.
         * Possible values typically include "correct", "incorrect", or "partial".
         */
        public String titleStatus;

        /**
         * The status indicating the correctness or overlap of the compared genres.
         * Possible values might include "correct" (all matched), "some_correct" (some matched), or "incorrect".
         */
        public String genresStatus;

        /**
         * The status indicating the correctness or overlap of the compared platforms.
         * Possible values might include "correct" (all matched), "some_correct" (some matched), or "incorrect".
         */
        public String platformsStatus;

        /**
         * The status indicating the relationship between the guessed rating and the target rating.
         * Possible values might include "correct", "higher", or "lower".
         */
        public String ratingStatus;

        /**
         * The status indicating the relationship between the guessed release year and the target year.
         * Possible values might include "correct", "earlier", or "later".
         */
        public String yearStatus;

        /**
         * A map containing key details of the guessed entity (e.g., the guessed game)
         * which are returned to the client for display or further processing.
         * Keys should be standardized field names.
         */
        public Map<String, Object> guessedGame;

        /**
         * Default, no-argument constructor.
         * This is necessary for easy object instantiation, particularly by deserialization libraries (like Jackson).
         */
        public ComparisonResponse() {}
    }

    /**
     * Maps a single row from a JDBC ResultSet to a {@link Game} object.
     * Handles type conversions for dates, ratings (BigDecimal), and array fields (genres, platforms).
     *
     * @param rs The ResultSet row to map.
     * @return A fully populated {@link Game} object.
     * @throws SQLException If a database access error occurs.
     */
    private Game mapRowToGame(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String externalId = rs.getString("external_id");
        String title = rs.getString("title");
        String description = rs.getString("description");

        java.sql.Date rd = rs.getDate("release_date");
        LocalDate releaseDate = rd == null ? null : rd.toLocalDate();

        String backgroundImage = rs.getString("background_image");

        Double rating = null;
        BigDecimal bd = rs.getBigDecimal("rating");
        if (bd != null) rating = bd.doubleValue();

        List<String> genres = null;
        Array genresArray = rs.getArray("genres");
        if (genresArray != null) {
            Object a = genresArray.getArray();
            if (a instanceof String[]) genres = Arrays.asList((String[]) a);
            else {
                Object[] oarr = (Object[]) a;
                genres = new ArrayList<>();
                for (Object o : oarr) genres.add(o == null ? null : o.toString());
            }
        }

        List<String> platforms = null;
        Array platArray = rs.getArray("platforms");
        if (platArray != null) {
            Object a = platArray.getArray();
            if (a instanceof String[]) platforms = Arrays.asList((String[]) a);
            else {
                Object[] oarr = (Object[]) a;
                platforms = new ArrayList<>();
                for (Object o : oarr) platforms.add(o == null ? null : o.toString());
            }
        }

        Timestamp createdAt = rs.getTimestamp("created_at");

        return new Game(id, externalId, title, description, releaseDate, backgroundImage, rating, genres, platforms, createdAt);
    }

    /**
     * Compares the user's guessed game with the correct game, providing status codes for various attributes.
     * <p>
     * Logic includes: title matching (exact, partial), genre/platform intersection, and numerical rating/year difference.
     *
     * @param guessTitle The title guessed by the user.
     * @param correctTitle The title of the correct game.
     * @return A {@link ComparisonResponse} detailing the match status for each field.
     */
    public ComparisonResponse compareGames(String guessTitle, String correctTitle) {
        Game guessed = getGameByTitle(guessTitle);
        Game correct = getGameByTitle(correctTitle);

        ComparisonResponse resp = new ComparisonResponse();

        if (guessed == null) {
            List<String> partialMatches = searchGamesByTitle(guessTitle);
            if (!partialMatches.isEmpty()) {
                guessed = getGameByTitle(partialMatches.get(0));
            } else {
                resp.titleStatus = "unknown";
                return resp;
            }
        }

        if (correct == null) {
            resp.titleStatus = "error";
            return resp;
        }

        String guessedLower = guessed.getTitle().toLowerCase();
        String correctLower = correct.getTitle().toLowerCase();

        if (guessedLower.equals(correctLower)) {
            resp.titleStatus = "green";
        } else if (guessedLower.contains(correctLower) || correctLower.contains(guessedLower)
                || hasCommonWords(guessedLower, correctLower)) {
            resp.titleStatus = "yellow";
        } else {
            resp.titleStatus = "red";
        }

        resp.genresStatus = compareListsGenresStatus(correct.getGenres(), guessed.getGenres());
        resp.platformsStatus = compareListsGenresStatus(correct.getPlatforms(), guessed.getPlatforms());
        resp.ratingStatus = compareNumericRatingStatus(correct.getRating(), guessed.getRating());
        resp.yearStatus = compareYearStatus(correct.getReleaseYear(), guessed.getReleaseYear());

        Map<String,Object> g = new HashMap<>();
        g.put("title", guessed.getTitle());
        g.put("genres", guessed.getGenres());
        g.put("platforms", guessed.getPlatforms());
        g.put("rating", guessed.getRating());
        g.put("releaseYear", guessed.getReleaseYear());
        resp.guessedGame = g;

        return resp;
    }

    /**
     * Helper method to check if the guessed title and the correct title share any common words (case-insensitive).
     *
     * @param guessed The user's guessed title (lowercase).
     * @param correct The correct game title (lowercase).
     * @return true if there is at least one common word, false otherwise.
     */
    public boolean hasCommonWords(String guessed, String correct) {
        Set<String> guessedWords = new HashSet<>(Arrays.asList(guessed.split("\\s+")));
        Set<String> correctWords = new HashSet<>(Arrays.asList(correct.split("\\s+")));
        guessedWords.retainAll(correctWords);
        return !guessedWords.isEmpty();
    }

    /**
     * Compares two lists (genres or platforms) and determines the status.
     * <ul>
     * <li>"green": Lists are identical.</li>
     * <li>"yellow": Lists have at least one common item (partial match).</li>
     * <li>"red": Lists have no common items, or one list is empty/null.</li>
     * </ul>
     *
     * @param actual The correct list of items.
     * @param guessed The user's list of items (from the guessed game).
     * @return The status string.
     */
    public String compareListsGenresStatus(List<String> actual, List<String> guessed) {
        if (actual == null || actual.isEmpty()) return "red";
        if (guessed == null || guessed.isEmpty()) return "red";

        Set<String> a = new HashSet<>();
        for (String s : actual) if (s != null) a.add(s.toLowerCase().trim());

        Set<String> g = new HashSet<>();
        for (String s : guessed) if (s != null) g.add(s.toLowerCase().trim());

        if (a.equals(g)) return "green";
        for (String s : g) {
            if (a.contains(s)) return "yellow";
        }
        return "red";
    }

    /**
     * Compares two double values (ratings) and determines the status based on the difference.
     * <ul>
     * <li>"green": Difference is less than 0.5.</li>
     * <li>"yellow": Difference is less than or equal to 1.0.</li>
     * <li>"red": Difference is greater than 1.0, or a value is null.</li>
     * </ul>
     *
     * @param actual The correct rating.
     * @param guessed The guessed game's rating.
     * @return The status string.
     */
    public String compareNumericRatingStatus(Double actual, Double guessed) {
        if (actual == null || guessed == null) return "red";
        double diff = Math.abs(actual - guessed);
        if (diff < 0.5) return "green";
        if (diff <= 1.0) return "yellow";
        return "red";
    }

    /**
     * Compares two-year values (release years) and determines the status.
     * <ul>
     * <li>"green": Years are identical.</li>
     * <li>"yellow": Difference is 1 year.</li>
     * <li>"lower": Guessed year is earlier than the actual year.</li>
     * <li>"higher": Guessed year is later than the actual year.</li>
     * <li>"red": Difference is greater than 1 year, or a value is null.</li>
     * </ul>
     *
     * @param actualYear The correct release year.
     * @param guessedYear The guessed game's release year.
     * @return The status string.
     */
    public String compareYearStatus(Integer actualYear, Integer guessedYear) {
        if (actualYear == null || guessedYear == null) return "red";
        if (actualYear.equals(guessedYear)) return "green";
        if (Math.abs(actualYear - guessedYear) <= 1) return "yellow";
        if (guessedYear < actualYear) return "lower";
        return "higher";
    }
}
