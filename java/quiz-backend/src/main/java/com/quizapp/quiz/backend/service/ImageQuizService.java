package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Game;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.math.BigDecimal;

public class ImageQuizService {
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

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

    public Game getGameById(int id) {
        String sql = """
            SELECT id, external_id, title, description, release_date, background_image, rating, genres, platforms, created_at
            FROM games
            WHERE id = ?
            LIMIT 1
        """;
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRowToGame(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ComparisonResponse {
        public String titleStatus;
        public String genresStatus;
        public String platformsStatus;
        public String ratingStatus;
        public String yearStatus;
        public Map<String, Object> guessedGame;

        public ComparisonResponse() {}
    }

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

        resp.genresStatus = compareListsStatus(correct.getGenres(), guessed.getGenres());
        resp.platformsStatus = compareListsStatus(correct.getPlatforms(), guessed.getPlatforms());
        resp.ratingStatus = compareNumericStatus(correct.getRating(), guessed.getRating());
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

    private boolean hasCommonWords(String guessed, String correct) {
        Set<String> guessedWords = new HashSet<>(Arrays.asList(guessed.split("\\s+")));
        Set<String> correctWords = new HashSet<>(Arrays.asList(correct.split("\\s+")));
        guessedWords.retainAll(correctWords);
        return !guessedWords.isEmpty();
    }

    private String compareListsStatus(List<String> actual, List<String> guessed) {
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

    private String compareNumericStatus(Double actual, Double guessed) {
        if (actual == null || guessed == null) return "red";
        double diff = Math.abs(actual - guessed);
        if (diff < 0.5) return "green";
        if (diff <= 1.0) return "yellow";
        return "red";
    }

    private String compareYearStatus(Integer actualYear, Integer guessedYear) {
        if (actualYear == null || guessedYear == null) return "red";
        if (actualYear.equals(guessedYear)) return "green";
        if (Math.abs(actualYear - guessedYear) <= 1) return "yellow";
        if (guessedYear < actualYear) return "lower";
        return "higher";
    }
}
